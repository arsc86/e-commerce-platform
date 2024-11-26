import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AccessComponent } from './access/access.component';
import { GuardGuard } from '../guards/guard.guard';
import { AccessVerifiedComponent } from './access/access-verified.component';
import { RecoveryPasswordComponent } from './recovery-password/recovery-password.component';
import { ChangePasswordComponent } from './recovery-password/change-password.component';

export const routes: Routes = [
    { path: 'login',  component: LoginComponent ,canActivate: [GuardGuard] },
    { path: 'validate/email',    component: AccessComponent },
    { path: 'verified/account/:token',  component: AccessVerifiedComponent },
    { path: 'verified/email',  component: AccessVerifiedComponent },
    { path: 'change/password',  component: ChangePasswordComponent },
    { path: 'recovery/password',  component: RecoveryPasswordComponent },
    { path: 'verified/recovery/password/:token',  component: RecoveryPasswordComponent },
    { path: '**', redirectTo: '/notfound' }
  ];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthRoutingModule { }
