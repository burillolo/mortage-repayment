# Development Setup Guide

This guide explains how to run the mortgage calculator application in development mode with both frontend and backend servers.

## Prerequisites

- Java 17
- Node.js (v18 or higher)
- Maven 3.6+

## Development Workflow

### Option 1: Separate Terminals (Recommended)

**Terminal 1 - Backend (Spring Boot):**
```bash
cd /Users/buril/projects/react/mortage-repayment
mvn spring-boot:run
```
- Backend will be available at: http://localhost:8080
- API endpoints: http://localhost:8080/api

**Terminal 2 - Frontend (Angular with Proxy):**
```bash
cd /Users/buril/projects/react/mortage-repayment/src/main/frontend/cuadro-web
npm start
```
- Frontend will be available at: http://localhost:4200
- API calls will be automatically proxied to backend

### Option 2: Maven Profile (All-in-One)

**Single Command:**
```bash
cd /Users/buril/projects/react/mortage-repayment
mvn spring-boot:run -Pfrontend
```
- Builds Angular frontend and serves it through Spring Boot
- Available at: http://localhost:8080
- No proxy needed (served from same origin)

## Proxy Configuration

The Angular development server is configured to proxy API requests:

**proxy.conf.js:**
```javascript
const PROXY_CONFIG = {
  "/api/*": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "debug"
  }
};
```

**What this does:**
- Any request to `http://localhost:4200/api/*` gets forwarded to `http://localhost:8080/api/*`
- Handles CORS issues during development
- Provides detailed logging of proxy requests

## Troubleshooting

### API Requests Failing

1. **Check Backend is Running:**
   ```bash
   curl http://localhost:8080/api?principal=100000&interest=5.5&duration=24&page=0&size=20
   ```

2. **Check Proxy Configuration:**
   - Ensure `proxy.conf.js` exists in frontend directory
   - Verify `npm start` uses `--proxy-config proxy.conf.js`

3. **Check Browser Network Tab:**
   - Look for failed requests to `/api/*`
   - Check if requests are being proxied correctly

### CORS Issues

If you see CORS errors in the browser console:
- Use the proxy configuration (Option 1)
- Or add CORS configuration to Spring Boot backend

### Port Conflicts

- Backend: Change port in `application.properties` (server.port=8081)
- Frontend: Use `ng serve --port 4201` or update proxy target

## File Structure

```
mortage-repayment/
├── src/main/java/          # Spring Boot backend
├── src/main/frontend/      # Angular frontend
│   └── cuadro-web/
│       ├── proxy.conf.js   # Proxy configuration
│       ├── package.json    # Frontend dependencies
│       └── src/app/        # Angular components
└── pom.xml                 # Maven configuration
```

## Development Tips

1. **Hot Reload:** Both servers support hot reload
   - Angular: Automatic on file changes
   - Spring Boot: Use `spring-boot-devtools` for automatic restart

2. **API Testing:** Use browser dev tools or Postman to test API endpoints

3. **Logs:** Check console output for proxy request/response logs

4. **Build:** Use `npm run build` to create production build for Maven profile
