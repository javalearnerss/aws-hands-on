import {
  Component,
  OnInit
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

import {
  FlightSearchService
} from '../../services/flight-search.service';

import {
  AnalyticsService
} from '../../services/analytics.service';

import {
  FlightResponse
} from '../../models/flight-response';

import {
  AnalyticsDashboard
} from '../../models/analytics-dashboard';

@Component({
  standalone: true,

  selector: 'app-flight-search',

  templateUrl: './flight-search.component.html',

  styleUrls: ['./flight-search.component.css'],

  imports: [
    FormsModule,
    CommonModule
  ]
})
export class FlightSearchComponent
implements OnInit {

  searchRequest = {

    from: 'Bangalore',
    to: 'Delhi',
    travelDate: '2026-05-05',
    travelerCount: 1
  };

  flights: FlightResponse[] = [];

  dashboard?: AnalyticsDashboard;

  loading = false;

  constructor(
    private flightSearchService:
      FlightSearchService,

    private analyticsService:
      AnalyticsService
  ) {
  }

  ngOnInit(): void {

    this.loadAnalytics();
  }

  searchFlights(event?: Event): void {

    event?.preventDefault();

    this.loading = true;

    this.flightSearchService
      .searchFlights(this.searchRequest)
      .subscribe({

        next: (response) => {

          this.flights = response.flights;

          this.loadAnalytics();

          this.loading = false;
        },

        error: (error) => {

          console.error(
            'Flight Search Error',
            error
          );

          this.loading = false;
        }
      });
  }

  loadAnalytics(): void {

    this.analyticsService
      .getDashboard()
      .subscribe({

        next: (response) => {

          this.dashboard = response;
        },

        error: (error) => {

          console.error(
            'Analytics Load Error',
            error
          );
        }
      });
  }

  trackByFlight(
    index: number,
    flight: FlightResponse
  ): string {

    return flight.flightNo;
  }
}