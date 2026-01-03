# Postman Testing Instructions

## Prerequisites

1.  **Postman Installed:** Download from [postman.com/downloads](https://www.postman.com/downloads/).
2.  **Application Running:** Ensure your Spring Boot application is running locally.
    * Default URL: `http://localhost:8080`

## Setup Guide

### 1. Import Collection
1.  Open Postman.
2.  Click the **Import** button (top left).
3.  Drag and drop the `.json` collection file from this folder into Postman.
4.  *(Optional)* If an Environment file is provided, import that as well.

### 2. Verify Connection
Before running the scripts, ensure your API is accessible:
* Open the request **"Get All Users"**.
* Click **Send**.
* You should receive a `200 OK` response with a JSON list.