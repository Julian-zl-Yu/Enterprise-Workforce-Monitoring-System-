<template>
  <div v-loading="isLoading" class="map-view">
    <div class="address-input-wrapper">
      <el-select
        ref="select"
        class="address-input"
        :value="positionInfo.address"
        filterable
        remote
        reserve-keyword
        placeholder="请输入关键词用以查询"
        :remote-method="remoteMethod"
        :loading="loading"
        @change="handleSelectChange"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <Btn
        class="address-input-btn"
        :configs="{ text: '确定', type: BTN_TYPE.save, click: saveInfo }"
      />
    </div>
    <div
      style="z-index: 20000"
      id="idAMapPositionPickerMap"
      ref="map"
      class="map-instance"
    />
  </div>
</template>
<script>
import _merge from "lodash/merge";
import _filter from "lodash/filter";
import _debounce from "lodash/debounce";
import isString from "lodash/isString";
import { GetLib } from "@/utils/libScript";
import { GLOBAL_LIB } from "@/components/Types";
import { Position } from "./Position";
import { $id } from "@/utils/iQuery";

let AMap = false;

export default {
  name: "MapView",
  inject: ["PositionPicker"],
  data() {
    return {
      loading: false,
      options: [],
      /* 高德地图工具实例 */
      placeSearch: false,
      placeSearchSearch: false,
      mapInstance: false,
      geolocation: false,
      district: false,
      /* 高德地图工具实例 */
      selectedMarker: false,
      isLoading: false,
      positionInfo: Position.empty(),
      cachePosition: Position.empty(),
    };
  },
  async mounted() {
    this.isLoading = true;
    try {
      /* 获取基础的地图工具 */
      await this.initAMap();
      /* 获取经纬度 */
      /* 有没有address */
      if (this.PositionPicker.positionInfo.address) {
        await this.setPositionByAddressNameOrLngLat();
      } else if (
        isString(this.PositionPicker.range) &&
        this.PositionPicker.range
      ) {
        /* CForm=>formData=>FormItem=>prop=>range(areacode) */
        await this.setPositionByAreacode(this.PositionPicker.range);
      } else {
        await this.setPositionByWebApi();
      }
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoading = false;
    }
  },
  destroyed() {
    $id("root-element").style.zIndex = "unset";
  },
  methods: {
    handleSelectChange(id) {
      /* 写入positionInfo */
      var positionInfo = _filter(this.remoteMethod.pois, (poi) => {
        return poi.id === id;
      })[0];
      if (positionInfo) {
        positionInfo = new Position(
          positionInfo.location.lng,
          positionInfo.location.lat
        );
      }
      this.setPositionByAddressNameOrLngLat(positionInfo).catch((error) => {
        this.$message.error(error.message);
      });
    },
    remoteMethod(query) {
      this.loading = true;
      this.placeSearchSearch(query, (status, result) => {
        this.loading = false;
        // 查询成功时，result即对应匹配的POI信息
        var pois = result.poiList.pois;
        this.remoteMethod.pois = pois;
        this.options = pois.map((poi) => {
          return {
            label: poi.name,
            value: poi.id,
          };
        });
      });
    },
    async setPositionByAreacode(areacode) {
      const center = await new Promise((resolve, reject) => {
        this.district.search(areacode, (status, result) => {
          if (status !== "complete") {
            reject(result);
          }
          if (status) {
            var center = result.districtList[0].center;
          }
          //视口自适应
          resolve(center);
        });
      });
      this.positionInfo = await this.lonlatToAddress({
        longitude: center.lng,
        latitude: center.lat,
      });
      this.location(this.positionInfo);
    },
    async setPositionByAddressNameOrLngLat(argPositionInfo) {
      const positionInfo = argPositionInfo
        ? argPositionInfo
        : _merge({}, this.PositionPicker.positionInfo);

      /* 经纬度与地址名称三者不全 */
      if (
        !(
          positionInfo.longitude &&
          positionInfo.latitude &&
          positionInfo.address
        )
      ) {
        /* 优先以经纬度为准 */
        const methodName =
          positionInfo.longitude && positionInfo.latitude
            ? "lonlatToAddress"
            : "addressToLonlat";
        this.positionInfo = await this[methodName](positionInfo);
      } else {
        this.positionInfo = positionInfo;
      }
      this.location(this.positionInfo);
    },
    async setPositionByWebApi() {
      const result = await new Promise((resolve, reject) => {
        this.geolocation.getCurrentPosition((status, result) => {
          if (status == "complete") {
            resolve(result);
          } else {
            reject(result);
          }
        });
      });
      const lnglat = result.position;
      this.positionInfo.address = result.formattedAddress;
      this.positionInfo.longitude = lnglat.lng;
      this.positionInfo.latitude = lnglat.lat;
    },
    saveInfo() {
      this.PositionPicker.positionInfo = _merge({}, this.positionInfo);
      this.PositionPicker.closeMapView();
    },
    async initAMap() {
      AMap = await GetLib(GLOBAL_LIB.AMap);
      this.geocoder = new AMap.Geocoder();
      const mapInstance = (this.mapInstance = new AMap.Map(this.$refs.map, {
        zoom: 15,
      }));
      AMap.plugin(
        ["AMap.ToolBar", "AMap.Scale", "AMap.OverView", "AMap.PlaceSearch"],
        () => {
          this.placeSearch = new AMap.PlaceSearch({});
          this.placeSearchSearch = _debounce(
            this.placeSearch.search.bind(this.placeSearch),
            600
          );
          mapInstance.addControl(new AMap.ToolBar());
          mapInstance.addControl(new AMap.Scale());
          mapInstance.addControl(new AMap.OverView({ isOpen: true }));
        }
      );
      mapInstance.on("click", this.mapClickClickHandler.bind(this));

      await Promise.all([
        /* 地图初始IP城市定位 */
        new Promise((resolve) => {
          mapInstance.plugin("AMap.Geolocation", () => {
            this.geolocation = new AMap.Geolocation({
              enableHighAccuracy: true,
              timeout: 3000,
              maximumAge: 0,
              convert: true,
              panToLocation: true,
              zoomToAccuracy: true,
              markerOptions: {},
            });
            mapInstance.addControl(this.geolocation);
          });
          resolve();
        }),
        /* 行政区查询 */
        new Promise((resolve) => {
          AMap.plugin("AMap.DistrictSearch", () => {
            //实例化DistrictSearch
            this.district = new AMap.DistrictSearch({
              subdistrict: 0, //获取边界不需要返回下级行政区
              extensions: "all", //返回行政区边界坐标组等具体信息
              level: "district", //查询行政级别为 市
            });
            resolve();
          });
        }),
      ]);
    },
    location(position) {
      this.selectMarker(this.createMarkerFromPosition(position));
    },
    addressToLonlat(position) {
      return new Promise((resolve, reject) => {
        this.geocoder.getLocation(position.address, (status, result) => {
          if (status === "complete" && result.geocodes.length) {
            var lnglat = result.geocodes[0].location;
            position.longitude = lnglat.lng;
            position.latitude = lnglat.lat;
            resolve(position);
          } else {
            reject("根据地址查询位置失败: " + status);
          }
        });
      });
    },
    lonlatToAddress(position) {
      return new Promise((resolve, reject) => {
        const lnglat = `${position.longitude},${position.latitude}`;
        this.geocoder.getAddress(
          // "116.39,39.9",
          lnglat,
          (status, result) => {
            if (status === "complete" && result.regeocode) {
              position.address = result.regeocode.formattedAddress;
              resolve(position);
            } else {
              reject("根据经纬度查询地址失败: " + status);
            }
          }
        );
      });
    },
    mapClickClickHandler(e) {
      this.mapInstance.clearMap();
      this.positionInfo.longitude = e.lnglat.lng;
      this.positionInfo.latitude = e.lnglat.lat;
      this.location(new Position(e.lnglat.lng, e.lnglat.lat, "", ""));
    },
    createMarkerFromPosition(position) {
      const markerOpts = {
        map: this.mapInstance,
        position: new AMap.LngLat(position.longitude, position.latitude),
        topWhenClick: true,
        offset: new AMap.Pixel(-5, -30),
        animation: "AMAP_ANIMATION_DROP",
        extData: position,
      };
      if (position.address) {
        markerOpts.title = position.address;
      }
      return new AMap.Marker(markerOpts);
    },
    clearPosition() {
      this.cachePosition = Position.empty();
      if (this.selectedMarker) {
        this.selectedMarker.setMap(null);
      }
      this.mapInstance.address = "";
    },
    selectMarker(marker) {
      this.clearPosition();
      this.selectedMarker = marker;
      var position = marker.getExtData();
      var lngLat = marker.getPosition();

      this.cachePosition.longitude = position.longitude;
      this.cachePosition.latitude = position.latitude;

      if (!position.address) {
        let geocoder = false;
        this.mapInstance.plugin(["AMap.Geocoder"], () => {
          geocoder = new AMap.Geocoder({
            radius: 1000,
            extensions: "base",
          });
          AMap.event.addListener(geocoder, "complete", (GeocoderResult) => {
            if (GeocoderResult["info"] == "OK") {
              var address = GeocoderResult.regeocode.formattedAddress;
              position.address = address;
              this.selectedMarker.setExtData(position);
              this.cachePosition.address = address;
              this.positionInfo.address = address;
              this.mapInstance.panTo(lngLat);
            }
          });

          geocoder.getAddress(lngLat);
        });
      } else {
        this.cachePosition.address = position.address;
        this.mapInstance.panTo(lngLat);
        this.positionInfo.address = position.address;
      }
    },
    getCurrentPosition() {
      return new Promise((resolve) => {
        if ("geolocation" in navigator) {
          navigator.geolocation.getCurrentPosition((location) => {
            resolve(
              _merge({}, this.positionInfo, {
                longitude: location.coords.longitude,
                latitude: location.coords.latitude,
              })
            );
          });
        } else {
          resolve(false);
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.map-view {
  margin: 2px;
  width: 100%;
  display: flex;
  flex-flow: column nowrap;

  .address-input-wrapper {
    margin: 6px;
    display: flex;
    flex-flow: row nowrap;
    align-items: center;
    .address-input {
      flex: 1;
    }
  }
  
  .map-instance {
    margin: 6px;
    height: 0;
    flex: 1;
  }
}

</style>