import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { ProfileComponent } from './profile/profile.component';
import { SignupComponent } from './signup/signup.component';
import { FormsModule } from '@angular/forms';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { CalendarModule } from 'primeng/calendar';
import { ChipsModule } from 'primeng/chips';
import { DropdownModule } from 'primeng/dropdown';
import { InputMaskModule } from 'primeng/inputmask';
import { InputNumberModule } from 'primeng/inputnumber';
import { CascadeSelectModule } from 'primeng/cascadeselect';
import { MultiSelectModule } from 'primeng/multiselect';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { TabViewModule } from 'primeng/tabview';
import { ChipModule } from 'primeng/chip';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { GoogleMapsModule } from '@angular/google-maps';
import { InputGroupModule } from 'primeng/inputgroup';


@NgModule({
  declarations: [
    ProfileComponent,
    SignupComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    FormsModule,
    /*AutoCompleteModule,
    CalendarModule,
    ChipsModule,
    DropdownModule,
    InputMaskModule,
    InputNumberModule,
    CascadeSelectModule,
    MultiSelectModule,*/
    //InputTextareaModule,
    TableModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    MenuModule,
    TabViewModule,
    ChipModule,
    CalendarModule,
    InputNumberModule,
    DialogModule,
    DropdownModule,
    InputMaskModule,
    GoogleMapsModule,
    InputGroupModule
  ]
})
export class UserModule { }
