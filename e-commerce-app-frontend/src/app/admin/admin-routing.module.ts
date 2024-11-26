import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RolesComponent } from './roles/roles.component';
import { UsersComponent } from './users/users.component';
import { AppLayoutComponent } from '../layout/app.layout.component';
import { GuardGuard } from '../guards/guard.guard';
import { Constants } from '../interfaces/general/constants';
import { RegionComponent } from './region/region.component';
import { ProviderComponent } from './provider/provider.component';
import { WarehouseComponent } from './warehouse/warehouse.component';

const routes: Routes = [
  {
    path: '', component: AppLayoutComponent,
    children: [
      { path: 'roles', component: RolesComponent, 
              canActivate: [GuardGuard], 
              data: { roles: [Constants.ROLES.ADMIN] } 
      },
      { path: 'region', component: RegionComponent, 
              canActivate: [GuardGuard], 
              data: { roles: [Constants.ROLES.ADMIN] } 
      },
      { path: 'provider', component: ProviderComponent, 
              canActivate: [GuardGuard], 
              data: { roles: [Constants.ROLES.ADMIN] } 
      },
      { path: 'warehouse', component: WarehouseComponent, 
              canActivate: [GuardGuard], 
              data: { roles: [Constants.ROLES.ADMIN,Constants.ROLES.PROVIDER] } 
      },
      { path: 'users', component: UsersComponent,
              canActivate: [GuardGuard], 
              data: { roles: [Constants.ROLES.ADMIN] } 
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
