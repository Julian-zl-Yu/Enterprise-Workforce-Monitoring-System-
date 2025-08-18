# Enterprise Workforce Monitoring System

A workforce monitoring platform designed for enterprises and multi-level regulatory agencies.  
The platform enables enterprises to report employee headcount monthly and allows regulatory agencies to review and analyze workforce statistics.

---

## 3 Main User Roles

### 1. Enterprise

**Enterprise Registration**
- User fills in name, contact info, business number, company name, location, industry, sector, and size.  
- System checks info and ensures the uniqueness of the business number.  
- After registration, the system uses Tianyancha to automatically import additional company information.  
- SMS text is sent to the user for communication and password reset.  

**Update Enterprise Information**
- Enterprises can update information in the corresponding section.  
- Updates require approval (status changes to *“waiting for review”*).  

**Report Employment Status**
- Enterprises submit monthly workforce status reports.  
- If no submission, the system automatically uses last month’s data.  
- If already submitted, a new submission will overwrite the old data.  
- Reports must be submitted within a predefined time frame.  

**View Historical Employment Reports**
- Enterprises can view historical employment reports.  
- Trends and other statistics are displayed for reference.  

---

### 2. Regulatory Agencies

**Agency Registration**
- Developers collect data about agencies and distribute accounts and levels of authority.  

**Review and Manage Enterprises**
- Agencies can only view enterprises under their level of supervision.  
- Agencies can review or submit requests to modify enterprise information.  

**Review Monthly Workforce Reports**
- Agencies can view and review enterprises’ monthly workforce reports.  
- Agencies can approve or disapprove of reports. Once approved, reports cannot be modified.  
- If no review is made, the report is automatically approved.  

**View Statistics**
- Agencies can view 8 statistical tables summarizing workforce data.  
- Data can be viewed as a whole or grouped by different categories (e.g., time, location, industry).  

---

### 3. System Administrators

**Access**
- Administrators have access to all data and modules available to enterprises and agencies.  

**Manage User Authorities & Structure**
- View and/or modify user information, user types, and levels of agencies.  
- Assign and/or edit user access authorities.  

**Manage System Settings**
- Manage menus, parameters, dictionaries, and other system settings.  

**Access Logs**
- View and query all login, operation, and error logs.  

---
