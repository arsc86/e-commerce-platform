import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './login/login.component';
import { CheckboxModule } from 'primeng/checkbox';
import { PasswordModule } from 'primeng/password';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { AccessComponent } from './access/access.component';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { AccessVerifiedComponent } from './access/access-verified.component';
import { RecoveryPasswordComponent } from './recovery-password/recovery-password.component';
import { ChangePasswordComponent } from './recovery-password/change-password.component';

@NgModule({
    declarations: [
        LoginComponent,
        AccessComponent,
        AccessVerifiedComponent,
        RecoveryPasswordComponent,
        ChangePasswordComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        AuthRoutingModule,
        CheckboxModule,
        PasswordModule,
        ButtonModule,
        RippleModule,
        InputGroupModule,
        InputGroupAddonModule
    ]
})
export class AuthModule { }
