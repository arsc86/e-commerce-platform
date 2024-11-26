import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppLayoutComponent } from '../layout/app.layout.component';
import { CategoryComponent } from './category/category.component';
import { GuardGuard } from '../guards/guard.guard';
import { Constants } from '../interfaces/general/constants';
import { CharacteristicComponent } from './characteristic/characteristic.component';
import { ProductComponent } from './product/product.component';

const routes: Routes = [
  {
    path: '', component: AppLayoutComponent,
    children: [
      { path: 'category', component: CategoryComponent, 
              canActivate: [GuardGuard], 
              data: { roles: [Constants.ROLES.ADMIN,Constants.ROLES.PROVIDER] } 
      },
      { path: 'characteristic', component: CharacteristicComponent, 
              canActivate: [GuardGuard], 
              data: { roles: [Constants.ROLES.ADMIN,Constants.ROLES.PROVIDER] } 
      },
      { path: 'product', component: ProductComponent, 
              canActivate: [GuardGuard], 
              data: { roles: [Constants.ROLES.ADMIN,Constants.ROLES.PROVIDER] } 
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CatalogRoutingModule { }
