import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FlightSearchComponent } from './components/flight-search/flight-search.component';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FlightSearchComponent, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'flight-search-ui';
}
