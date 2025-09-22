# Debugging Guide for Angular + Spring Boot Application

This guide explains how to debug your mortgage calculator application using Chrome DevTools with original source code.

## Prerequisites

- Chrome or Chromium-based browser
- Angular development server running with source maps
- Spring Boot backend running

## Setup for Source Map Debugging

### 1. Angular Configuration (Already Done)

The following configurations have been set up for debugging:

**angular.json:**
```json
{
  "options": {
    "sourceMap": true  // Enable source maps for development
  },
  "configurations": {
    "development": {
      "sourceMap": true,
      "optimization": false,
      "extractCss": false,
      "namedChunks": true,
      "extractLicenses": false,
      "vendorChunk": true,
      "buildOptimizer": false
    }
  }
}
```

**tsconfig.json:**
```json
{
  "compilerOptions": {
    "sourceMap": true  // TypeScript source maps
  }
}
```

### 2. Start Development Server

```bash
cd /Users/buril/projects/react/mortage-repayment/src/main/frontend/cuadro-web
npm start
```

This will start Angular with:
- Source maps enabled
- Development configuration
- Proxy to Spring Boot backend

## Chrome DevTools Debugging

### 1. Open DevTools

- **Chrome:** F12 or Ctrl+Shift+I (Windows/Linux) / Cmd+Option+I (Mac)
- **Right-click → Inspect** on any element

### 2. Enable Source Maps

1. Open **Settings** (gear icon in DevTools)
2. Go to **Preferences** → **Sources**
3. Enable:
   - ✅ **Enable JavaScript source maps**
   - ✅ **Enable CSS source maps**
   - ✅ **Auto-reload generated CSS**

### 3. Navigate to Sources Tab

In the **Sources** tab, you should now see:

```
webpack://
├── src/
│   ├── app/
│   │   ├── app.component.ts
│   │   ├── dashboard-simulator/
│   │   │   ├── dashboard-simulator.component.ts
│   │   │   ├── dashboard-simulator.component.html
│   │   │   └── dashboard-simulator.component.css
│   │   ├── tabla-cuotas/
│   │   │   ├── tabla-cuotas.component.ts
│   │   │   ├── tabla-cuotas.component.html
│   │   │   └── tabla-cuotas.component.css
│   │   └── cuotas-service.service.ts
│   └── main.ts
```

### 4. Set Breakpoints

1. **Click on line numbers** in your TypeScript files to set breakpoints
2. **Right-click** on breakpoints to:
   - Add conditions
   - Set logpoints
   - Disable/enable

### 5. Debugging Features

**Step Through Code:**
- **F10:** Step over
- **F11:** Step into
- **Shift+F11:** Step out
- **F8:** Continue

**Inspect Variables:**
- Hover over variables to see values
- Use **Watch** panel to monitor expressions
- Check **Scope** panel for local/closure variables

**Console Debugging:**
```typescript
// Add to your TypeScript code
console.log('Debug point:', variable);
debugger; // Pause execution here
```

## Common Debugging Scenarios

### 1. Form Submission Issues

**Set breakpoint in:**
```typescript
// dashboard-simulator.component.ts
onSubmit() {
  debugger; // Add this line
  if (this.addressForm.invalid) { 
    return; 
  }
  // ... rest of method
}
```

**Check:**
- Form validation state
- Form values
- Event emission

### 2. API Request Issues

**Set breakpoint in:**
```typescript
// cuotas-service.service.ts
obtenerPeriodosCuotas(nominal: number, interest: number, months: number, page: number = 0, size: number = 20) {
  debugger; // Add this line
  this.isLoading.next(true);
  return this.httpClient.get<PageResponse<Period>>('/api', {
    // ... rest of method
  });
}
```

**Check:**
- Request parameters
- HTTP response
- Error handling

### 3. Component Lifecycle Issues

**Set breakpoints in:**
```typescript
// tabla-cuotas.component.ts
ngOnInit(): void {
  debugger; // Add this line
}

ngOnChanges(changes: SimpleChanges): void {
  debugger; // Add this line
  if (changes['loanData'] && this.loanData) {
    this.loadLoanData(this.loanData.nominal, this.loanData.rate, this.loanData.duration);
  }
}
```

## Troubleshooting Source Maps

### If Source Maps Don't Appear:

1. **Check Angular Build:**
   ```bash
   ng build --configuration=development
   ```

2. **Verify Source Map Files:**
   Look for `.map` files in `dist/out-tsc/` directory

3. **Clear Browser Cache:**
   - Hard refresh: Ctrl+Shift+R (Windows/Linux) / Cmd+Shift+R (Mac)
   - Or open DevTools → Network → Disable cache

4. **Check DevTools Settings:**
   - Ensure source maps are enabled in DevTools settings

### If Breakpoints Don't Work:

1. **Verify File Paths:**
   - Source maps should point to original `.ts` files
   - Check webpack:// folder structure

2. **Check TypeScript Compilation:**
   ```bash
   ng build --configuration=development --verbose
   ```

3. **Restart Development Server:**
   ```bash
   # Kill current server
   pkill -f "ng serve"
   # Restart
   npm start
   ```

## Advanced Debugging

### 1. Network Tab Debugging

- Monitor API requests to `/api` endpoint
- Check request/response headers
- Verify proxy configuration

### 2. Console Debugging

```typescript
// Add to any component
ngOnInit() {
  console.group('Component Debug');
  console.log('Component initialized');
  console.log('Form valid:', this.addressForm?.valid);
  console.log('Current data:', this.currentData);
  console.groupEnd();
}
```

### 3. Performance Debugging

- Use **Performance** tab to profile
- Check **Memory** tab for leaks
- Monitor **Network** tab for slow requests

## Production vs Development

**Development (with source maps):**
```bash
npm start  # Uses development configuration
```

**Production (no source maps):**
```bash
ng build --configuration=production
```

## Quick Reference

| Task | Command | Description |
|------|---------|-------------|
| Start dev server | `npm start` | Angular with source maps + proxy |
| Build dev | `ng build --configuration=development` | Build with source maps |
| Build prod | `ng build --configuration=production` | Build without source maps |
| Kill server | `pkill -f "ng serve"` | Stop Angular server |

## Tips

1. **Use `debugger;` statements** for quick debugging
2. **Set conditional breakpoints** for specific scenarios
3. **Use console.group()** for organized logging
4. **Check the Call Stack** to understand execution flow
5. **Use the Scope panel** to inspect variable values
