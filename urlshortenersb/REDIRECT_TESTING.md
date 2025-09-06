# Redirect Functionality Testing Guide

## Overview
This document outlines how to test the redirect functionality in the Bitly clone project.

## Backend Testing

### 1. Start the Backend Server
```bash
cd urlshortenersb
mvn spring-boot:run
```

### 2. Test Redirect Endpoints

#### Test Valid Short URL (Should redirect to original URL)
```bash
curl -I http://localhost:8080/{shortUrl}
```
Expected Response:
- Status: 302 Found
- Location header: Original URL

#### Test Expired Short URL (Should return 410 Gone)
```bash
curl -I http://localhost:8080/{expiredShortUrl}
```
Expected Response:
- Status: 410 Gone

#### Test Non-existent Short URL (Should return 404 Not Found)
```bash
curl -I http://localhost:8080/nonexistent
```
Expected Response:
- Status: 404 Not Found

## Frontend Testing

### 1. Start the Frontend Server
```bash
cd url-shortener-frontend
npm run dev
```

### 2. Test Short URL Creation and Display
1. Login to the application
2. Create a new short URL
3. Verify that the displayed short URL shows the backend domain (http://localhost:8080/{shortCode})
4. Click on the short URL link
5. Verify it redirects to the original URL

### 3. Test Direct Browser Access
1. Copy the full short URL from the frontend
2. Paste it directly in a new browser tab
3. Verify it redirects to the original URL

## Expected Behavior

### Valid Short URL
- Clicking the short URL should redirect to the original URL
- The redirect should happen in the same browser tab/window
- Click count should be incremented

### Expired Short URL
- Should show a "410 Gone" error page
- Should not redirect to the original URL

### Non-existent Short URL
- Should show a "404 Not Found" error page
- Should not redirect anywhere

## Configuration

### Backend URL Configuration
The backend URL is configured in `application.properties`:
```properties
backend.url=${BACKEND_URL:http://localhost:8080}
```

### Environment Variables
Set the following environment variables if needed:
- `BACKEND_URL`: Backend server URL (default: http://localhost:8080)
- `FRONTEND_URL`: Frontend server URL

## Troubleshooting

### Common Issues
1. **CORS Issues**: Make sure CORS is properly configured
2. **Port Conflicts**: Ensure backend runs on 8080 and frontend on 5173
3. **Database Issues**: Ensure database is running and accessible
4. **URL Format**: Ensure short URLs are properly formatted

### Debug Steps
1. Check backend logs for errors
2. Verify database contains the short URL mapping
3. Test the redirect endpoint directly with curl
4. Check browser network tab for redirect responses

