<template>
  <div class="avatar-camera-inner-wrapper">
    <video
      v-show="isShowVideo"
      ref="video"
      autoplay
      id="videoCamera"
      :width="videoWidth"
      :height="videoHeight"
      class="avatar-camera video"
    />
    <div v-if="!isShowVideo" class="img_bg_camera avatar-camera image">
      <img :src="imgSrc" alt="" width="640px" height="480px" />
    </div>
    <canvas
      style="display:none;"
      id="canvasCamera"
      :width="videoWidth"
      :height="videoHeight"
    ></canvas>

    <!-- 拍照 -->
    <i
      v-show="isShowVideo"
      class="camera-btn el-icon-camera"
      @click="takePhoto"
    />

    <!-- 重拍 -->
    <i v-show="!isShowVideo" class="camera-btn el-icon-check" @click="ensure" />
    <!-- 确认 -->
    <i
      v-show="!isShowVideo"
      class="camera-btn el-icon-close"
      @click="reTakePhoto"
    />
  </div>
</template>

<script>
export default {
  name: "AvatarCamera",
  data() {
    return {
      isShowVideo: true,
      imageStyle: {},
      videoWidth: 640,
      videoHeight: 480,
      imgSrc: "",
      thisCancas: null,
      thisContext: null,
      thisVideo: null,
    };
  },
  mounted() {
    setTimeout(() => {
      var _this = this;
      _this.$parent.camera = this;
      this.thisCancas = document.getElementById("canvasCamera");
      this.thisContext = this.thisCancas.getContext("2d");
      this.thisVideo = document.getElementById("videoCamera");
      // 旧版本浏览器可能根本不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
      }
      // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象
      // 使用getUserMedia，因为它会覆盖现有的属性。
      // 这里，如果缺少getUserMedia属性，就添加它。
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function(constraints) {
          // 首先获取现存的getUserMedia(如果存在)
          var getUserMedia =
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.getUserMedia;
          // 有些浏览器不支持，会返回错误信息
          // 保持接口一致
          if (!getUserMedia) {
            return Promise.reject(
              new Error("getUserMedia is not implemented in this browser")
            );
          }
          // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
          return new Promise(function(resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject);
          });
        };
      }
      var constraints = {
        audio: false,
        video: {
          width: this.videoWidth,
          height: this.videoHeight,
          transform: "scaleX(-1)",
        },
      };
      navigator.mediaDevices
        .getUserMedia(constraints)
        .then(function(stream) {
          // 旧的浏览器可能没有srcObject
          if ("srcObject" in _this.thisVideo) {
            _this.thisVideo.srcObject = stream;
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            _this.thisVideo.src = window.URL.createObjectURL(stream);
          }
          _this.thisVideo.onloadedmetadata = function(e) {
            _this.thisVideo.play();
          };
          _this.$parent.isLoading = false;
        })
        .catch((err) => {
          console.log(err.name + ": " + err.message);
          _this.$parent.isLoading = false;
        });
    }, 100);
  },
  methods: {
    takePhoto() {
      var _this = this;
      // 点击，canvas画图
      _this.thisContext.drawImage(
        _this.thisVideo,
        0,
        0,
        _this.videoWidth,
        _this.videoHeight
      );
      // 获取图片base64链接
      var image = this.thisCancas.toDataURL("image/jpg");
      _this.imgSrc = image;
      this.imgBase64 = _this.thisCancas.toDataURL();
      this.waitEnsure();
    },
    // base64转文件

    dataURLtoFile(dataurl, filename) {
      var arr = dataurl.split(",");
      var mime = arr[0].match(/:(.*?);/)[1];
      var bstr = atob(arr[1]);
      var n = bstr.length;
      var u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], filename, {
        type: mime,
      });
    },
    // 关闭摄像头

    stopNavigator() {
      this.thisVideo.srcObject.getTracks()[0].stop();
    },
    /* 等待确认结果 */
    waitEnsure() {
      this.isShowVideo = false;
      this.imageStyle = {
        backgroundImage: `url(${this.imgBase64})`,
      };
    },
    /* 再来一次 */
    reTakePhoto() {
      this.isShowVideo = true;
    },
    ensure() {
      /*确定之后*/
      this.$parent.imgBase64 = this.imgBase64;
      this.$parent.handleValueChange(this.imgBase64);
      this.close();
    },
    close(isParentCall) {
      this.thisVideo.srcObject.getTracks()[0].stop();
      this.theStream = null;
      this.$parent.isOpenCamera = false;
      this.$parent.isLoading = false;
      if (!isParentCall) {
        this.$parent.removeZIndex();
        this.$layer.close(this.$options.propsData.layerid);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.avatar-camera-inner-wrapper {
  height: 100%;
  width: 100%;
  display: flex;
  flex-flow: column nowrap;
  justify-content: center;
  align-items: center;
  position: relative;

  > .avatar-camera {
    z-index: 0;
    transform: scaleX(-1);
    width: 640px;
    height: 480px;
    &.image {
      background: center center/contain no-repeat;
    }
  }
  > .require-tips {
    z-index: 1;
    color: white;
    background-color: rgba(0, 0, 0, 0.96);
    position: absolute;
    top: 1rem;
    left: 1rem;
    width: 220px;
    border-radius: 4px;
    > .tips {
      margin: 0;
      padding: 4px;
      > li {
        margin: 6px;
        list-style: circle;
        text-indent: 10px;
        list-style-position: inside;
      }
    }
  }
  > .camera-btn {
    color: white;
    position: absolute;
    bottom: 1rem;
    font-size: 4rem;
    &.el-icon-check {
      right: 25%;
      color: green;
    }
    &.el-icon-close {
      left: 25%;
      color: red;
    }
    &:hover {
      cursor: pointer;
      text-shadow: 2px 2px 2px black;
    }
  }
}
</style>
