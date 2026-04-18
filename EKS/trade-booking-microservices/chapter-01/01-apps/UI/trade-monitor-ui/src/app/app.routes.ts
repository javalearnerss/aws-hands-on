import { Routes } from '@angular/router';
import { ExchangeComponent } from './components/exchange/exchange.component';
import { OpsDashboardComponent } from './components/ops-dashboard/ops-dashboard.component';
import { TradeEnricherComponent } from './components/trade-enricher/trade-enricher.component';
import { TradeProcessorComponent } from './components/trade-processor/trade-processor.component';
import { TradeRefDataComponent } from './components/trade-ref-data/trade-ref-data.component';
import { HomeComponent } from './components/home/home.component'; 
import { CreateUserComponent } from './components/user-management/create-user/create-user.component';
import { EditUserComponent } from './components/user-management/edit-user/edit-user.component';
import { UserManagementComponent } from './components/user-management/user-management.component';


export const routes: Routes = [
  {
    path: '',
    component: OpsDashboardComponent,
  },
  {
    path: 'ops-dashboard',
    component: OpsDashboardComponent
  },
  {
    path: 'exchange',
    component: ExchangeComponent
  },
  {
    path: 'trade-enricher',
    component: TradeEnricherComponent
  },
  {
    path: 'trade-processor',
    component: TradeProcessorComponent
  },
  {
    path: 'refdata-provider',
    component: TradeRefDataComponent
  },
  {
    path: 'users',
    component: UserManagementComponent,
    children: [
      {
        path: 'create',
        component: CreateUserComponent,
        data: { role: 'ADMIN' }
      },
      {
        path: 'create',
        component: CreateUserComponent,
        data: { role: 'ADMIN' }
      },

      {
        path: 'edit/:id',
        component: EditUserComponent,
        data: { role: 'ADMIN' }
      }
    ]
  }
];