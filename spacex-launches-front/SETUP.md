# SpaceX Launches Dashboard - Setup Guide

## Overview
This Angular application provides a dashboard for viewing SpaceX launch data, connecting to a backend API to display real-time launch statistics and recent launches.

## Prerequisites
- Node.js (v18 or higher)
- Angular CLI
- PowerShell execution policy configured

## PowerShell Execution Policy Fix
If you encounter PowerShell execution policy errors, run this command as Administrator:
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

## Installation & Running
1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm start
# or
ng serve
```

3. Open your browser to `http://localhost:4200`

## Project Structure
```
src/
├── app/
│   ├── components/
│   │   ├── layout/
│   │   │   ├── shell/          # Main layout wrapper
│   │   │   └── sidebar/        # Navigation sidebar
│   ├── features/
│   │   └── dashboard/          # Dashboard with launch statistics
│   ├── shared/
│   │   ├── models/             # TypeScript interfaces
│   │   └── services/           # HTTP services
│   ├── app.config.ts           # App configuration with HttpClient
│   └── app.routes.ts           # Routing configuration
```

## Features Implemented
✅ **Dashboard Component**: Displays launch statistics and recent launches
✅ **Launch Service**: HTTP service to connect to SpaceX backend API
✅ **TypeScript Models**: Interfaces for launch data
✅ **Responsive UI**: Modern design with Tailwind CSS
✅ **Error Handling**: Loading states and error messages
✅ **Real-time Data**: Refresh functionality for live updates

## API Integration
- **Backend URL**: `https://fwit88xlf0.execute-api.us-east-1.amazonaws.com/prod/api`
- **Endpoints**:
  - `GET /launches` - Get all launches
  - `GET /launches/{id}` - Get launch details
- **Features**:
  - Launch statistics calculation
  - Recent launches display
  - Status-based filtering
  - Error handling with retry functionality

## Dashboard Features
- **Statistics Cards**: Total launches, success rate, upcoming launches, next launch countdown
- **Recent Launches Grid**: Cards showing mission details, flight numbers, dates, and rocket info
- **Loading States**: Spinner and loading messages
- **Error Handling**: Error messages with retry buttons
- **Refresh Functionality**: Manual data refresh capability

## Next Steps
- Create breadcrumb component for mobile navigation
- Add reusable UI components (search, dropdowns, buttons)
- Implement launch detail views
- Add filtering and sorting capabilities
- Enhance mobile responsiveness
