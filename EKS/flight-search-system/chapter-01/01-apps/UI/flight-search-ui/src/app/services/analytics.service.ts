import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { AnalyticsDashboard }
from '../models/analytics-dashboard';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {

  private apiUrl =
    'http://localhost:7072/api/analytics/dashboard';

  constructor(private http: HttpClient) {
  }

  getDashboard():
    Observable<AnalyticsDashboard> {

    return this.http.get<AnalyticsDashboard>(
      this.apiUrl
    );
  }
}