// Launch Summary Model - for list views
export interface LaunchSummaryResponse {
  launchId: string;
  missionName: string;
  flightNumber: number;
  launchDateUtc: string;
  status: string;
  rocketId: string;
}

// Full Launch Model - for detailed views
export interface LaunchResponse {
  launchId: string;
  missionName: string;
  flightNumber: number;
  launchDateUtc: string;
  success: boolean;
  details: string;
  rocketId: string;
  launchpadId: string;
  payloads: string[];
  patchSmallLink: string;
  patchLargeLink: string;
  webcastLink: string;
  articleLink: string;
  wikipediaLink: string;
  status: string;
}

// Launch status enum for type safety
export enum LaunchStatus {
  SUCCESS = 'SUCCESS',
  FAILURE = 'FAILURE',
  UPCOMING = 'UPCOMING',
  IN_PROGRESS = 'IN_PROGRESS'
}

// Stats data response model
export interface StatsDataResponse {
  totalLaunches: number;
  successRate: number;
  successfulLaunches: number;
  failedLaunches: number;
  upcomingLaunches: number;
}

// API Response wrapper (if needed)
export interface ApiResponse<T> {
  data: T;
  success: boolean;
  message?: string;
}
