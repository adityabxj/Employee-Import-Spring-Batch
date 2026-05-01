# Employee Batch Processing System (Spring Boot + Spring Batch)

## 📌 Overview

This project is a **Spring Boot + Spring Batch application** designed to process employee data files (CSV/XLS) in bulk.

The system:

* Accepts a file in **Base64 format**
* Processes records using **Spring Batch**
* Validates data and checks duplicates
* Stores valid records in DB
* Generates:

  * ✅ Success file (valid/duplicate records)
  * ❌ Error file (invalid records)
* Tracks batch execution using a **Batch Job Tracker**

---

## 🚀 Features

* File upload via REST API
* Supports CSV and Excel files
* Chunk-based batch processing
* Data validation (empId, name, email)
* Duplicate record detection
* Success & Error file generation
* Batch tracking (STARTED → COMPLETED / FAILED)
* File cleanup scheduler (auto delete after 24 hours)

---

## 🏗️ Tech Stack

* Java 17
* Spring Boot
* Spring Batch
* Spring Data JPA
* MySQL
* Maven

---

## 📂 Project Structure

```
com.assignment.employee_batch
│
├── batch
│   ├── config        → Job & Step configuration
│   ├── reader        → Reads CSV / Excel
│   ├── processor     → Validation & business logic
│   ├── writer        → DB insert + file writing
│   ├── listener      → Job lifecycle tracking
│
├── controller        → REST APIs
├── service           → Business logic
├── repository        → DB layer
├── entity            → JPA entities
├── dto               → Request/Response models
├── util              → Helper utilities
├── scheduler         → File cleanup job
```

---

## 🔁 Processing Flow

```
Upload API
   ↓
Decode Base64 → Create Temp File
   ↓
Spring Batch Job Triggered
   ↓
Reader → Processor → Writer
   ↓
Files Generated (Success / Error)
   ↓
Batch Tracker Updated in DB
   ↓
Status API returns result
```

---

## 📡 API Documentation

### 🔹 1. Upload File API

**Endpoint:**

```
POST /api/upload
```

**Request:**

```json
{
  "file": "BASE64_STRING",
  "fileType": "csv"
}
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Batch started",
  "data": {
    "batchId": "uuid"
  }
}
```

**cURL:**

```
curl -X POST http://localhost:8080/api/upload \
-H "Content-Type: application/json" \
-d '{"file":"BASE64","fileType":"csv"}'
```

---

### 🔹 2. Batch Status API

**Endpoint:**

```
GET /api/status/{batchId}
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Batch status fetched",
  "data": {
    "status": "COMPLETED",
    "successFile": "BASE64",
    "errorFile": "BASE64"
  }
}
```

**cURL:**

```
curl http://localhost:8080/api/status/{batchId}
```

---

## 🧠 Validation Rules

* empId → required
* name → required
* email → valid format
* duplicate empId → marked as EXISTS

---

## 📄 Output Files

### ✅ Success File

Contains:

* Valid records
* Duplicate records (EXISTS)

### ❌ Error File

Contains:

* Invalid records
* Reason for failure

---

## 🗃️ Database Tables

### emp_details

Stores valid employee records

### batch_job_tracker

Tracks:

* batchId
* status
* successFilePath
* errorFilePath
* createdAt

---

## 🧹 File Cleanup Strategy

* Scheduled job runs every hour
* Deletes files older than **24 hours**
* Prevents disk storage issues

---

## ⚠️ Error Handling

* Invalid input → rejected at API level
* Invalid records → moved to error file
* Batch failures → marked as FAILED

---

## ▶️ How to Run

1. Clone project
2. Configure MySQL in `application.yml`
3. Run application
4. Use Postman to test APIs

---

## 💡 Future Enhancements

* Store files in AWS S3
* Add retry mechanism
* Add pagination in status API
* Improve validation with annotations

---

## 👨‍💻 Author

Vipin Chaurasiya
Java Developer (Spring Boot + Microservices)

---
