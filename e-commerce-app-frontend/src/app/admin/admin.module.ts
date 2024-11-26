import { CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { FileUploadModule } from 'primeng/fileupload';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { RatingModule } from 'primeng/rating';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { DropdownModule } from 'primeng/dropdown';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { RolesComponent } from './roles/roles.component';
import { BlockUIModule } from 'primeng/blockui';
import { UsersComponent } from './users/users.component';
import { RegionComponent } from './region/region.component';
import { ProviderComponent } from './provider/provider.component';
import { WarehouseComponent } from './warehouse/warehouse.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputMaskModule } from 'primeng/inputmask';

@NgModule({
  declarations: [
    RolesComponent,
    UsersComponent,
    RegionComponent,
    ProviderComponent,
    WarehouseComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,   
    TableModule,
    FileUploadModule,
    FormsModule,
    ButtonModule,
    RippleModule,
    ToastModule,
    ToolbarModule,
    RatingModule,
    InputTextModule,
    InputTextareaModule,
    DropdownModule,
    RadioButtonModule,
    InputNumberModule,
    DialogModule,
    BlockUIModule,
    GoogleMapsModule,
    InputGroupModule,
    InputMaskModule,
    FormsModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA,NO_ERRORS_SCHEMA]

})
export class AdminModule { }
