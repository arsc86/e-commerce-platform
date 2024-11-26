import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './signup/signup.component';
import { GuardGuard } from '../guards/guard.guard';
import { ProfileComponent } from './profile/profile.component';
import { Constants } from '../interfaces/general/constants';
import { AppLayoutComponent } from '../layout/app.layout.component';

const routes: Routes = [
  {
    path: '', component: AppLayoutComponent,
    children: [
      { path: 'profile', component: ProfileComponent, 
              canActivate: [GuardGuard], 
              data: { roles: [
                Constants.ROLES.ADMIN,
                Constants.ROLES.PROVIDER,
                Constants.ROLES.USER] 
              } 
      },
      { path: 'signup', component: SignupComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
