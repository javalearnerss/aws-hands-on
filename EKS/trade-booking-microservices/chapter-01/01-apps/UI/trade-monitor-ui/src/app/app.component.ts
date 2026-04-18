import { Component } from '@angular/core';
import { RouterOutlet, RouterModule, Router, NavigationEnd } from '@angular/router';
import { HeaderComponent } from "./components/header/header.component";
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, RouterOutlet, RouterOutlet, NgIf, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'trade-monitor-ui';

  showHeader = false;

  constructor(private router: Router) {

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {

        const hiddenRoutes = ['/', '/home'];

        this.showHeader = !hiddenRoutes.includes(event.urlAfterRedirects);
      });

  }


  ngOnInit(): void {
   

  }

  logout() {
    
  }


}
