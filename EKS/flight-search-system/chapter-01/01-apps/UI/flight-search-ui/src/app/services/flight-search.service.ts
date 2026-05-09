import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { FlightSearchRequest } from '../models/flight-search-request';
import { FlightSearchResponse } from '../models/flight-search-response';

@Injectable({
  providedIn: 'root'
})
export class FlightSearchService {

  private apiUrl =
    'http://localhost:7071/api/flights/search';

  constructor(private http: HttpClient) {
  }

  searchFlights(
    request: FlightSearchRequest
  ): Observable<FlightSearchResponse> {

    return this.http.post<FlightSearchResponse>(
      this.apiUrl,
      request
    );
  }
}