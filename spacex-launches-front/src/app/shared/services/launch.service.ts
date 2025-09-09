import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError, forkJoin } from 'rxjs';
import { catchError, map, switchMap, retry } from 'rxjs/operators';
import { LaunchSummaryResponse, LaunchResponse, StatsDataResponse, LaunchPageResponse } from '../models/launch.models';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LaunchService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  /**
   * Get all launches (summary data)
   */
  getAllLaunches(): Observable<LaunchSummaryResponse[]> {
    return this.http.get<LaunchSummaryResponse[]>(`${this.baseUrl}/launches`)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Get launch by ID (detailed data)
   */
  getLaunchById(id: string): Observable<LaunchResponse> {
    return this.http.get<LaunchResponse>(`${this.baseUrl}/launches/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Get recent launches (last 10)
   */
  getRecentLaunches(limit: number = 10): Observable<LaunchSummaryResponse[]> {
    return this.getAllLaunches().pipe(
      map(launches => launches
        .sort((a, b) => new Date(b.launchDateUtc).getTime() - new Date(a.launchDateUtc).getTime())
        .slice(0, limit)
      )
    );
  }

  /**
   * Get upcoming launches
   */
  getUpcomingLaunches(): Observable<LaunchSummaryResponse[]> {
    return this.getAllLaunches().pipe(
      map(launches => launches
        .filter(launch => launch.status === 'UPCOMING')
        .sort((a, b) => new Date(a.launchDateUtc).getTime() - new Date(b.launchDateUtc).getTime())
      )
    );
  }

  /**
   * Get launches by status
   */
  getLaunchesByStatus(status: string): Observable<LaunchSummaryResponse[]> {
    return this.getAllLaunches().pipe(
      map(launches => launches.filter(launch => launch.status === status))
    );
  }

  /**
   * Get launch statistics from dedicated stats endpoint
   */
  getStats(): Observable<StatsDataResponse> {
    return this.http.get<StatsDataResponse>(`${this.baseUrl}/launches/stats`)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Get launch statistics for dashboard
   */
  getLaunchStatistics(): Observable<StatsDataResponse> {
    return this.getStats();
  }


  /**
   * Get paginated launches
   */
  getPaginatedLaunches(pageNumber: number, pageSize: number, status?: string): Observable<LaunchPageResponse> {
    console.log('Getting paginated launches with:', { pageNumber, pageSize, status });
    
    // Since /launches/paginated doesn't exist, we'll use /launches and simulate pagination
    return this.getAllLaunches().pipe(
      map(launches => {
        // Filter by status if provided
        let filteredLaunches = launches;
        if (status) {
          filteredLaunches = launches.filter(launch => 
            launch.status.toUpperCase() === status.toUpperCase()
          );
        }
        
        // Calculate pagination
        const startIndex = pageNumber * pageSize;
        const endIndex = startIndex + pageSize;
        const paginatedContent = filteredLaunches.slice(startIndex, endIndex);
        
        // Create paginated response
        const response: LaunchPageResponse = {
          content: paginatedContent,
          number: pageNumber,
          size: pageSize,
          totalElements: filteredLaunches.length
        };
        
        console.log('Simulated pagination response:', response);
        return response;
      }),
      catchError(this.handleError)
    );
  }

  /**
   * Handle HTTP errors
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    
    console.error('LaunchService Error:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
