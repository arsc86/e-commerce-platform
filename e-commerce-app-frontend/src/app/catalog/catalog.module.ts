import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CatalogRoutingModule } from './catalog-routing.module';
import { CategoryComponent } from './category/category.component';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { DialogModule } from 'primeng/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { DropdownModule } from 'primeng/dropdown';
import { InputMaskModule } from 'primeng/inputmask';
import { CharacteristicComponent } from './characteristic/characteristic.component';
import { ProductComponent } from './product/product.component';
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { SidebarModule } from "primeng/sidebar";
import { TabMenuModule } from "primeng/tabmenu";
import { TabViewModule } from "primeng/tabview";
import { FileUploadModule } from "primeng/fileupload";


@NgModule({
  declarations: [
    CategoryComponent,
    CharacteristicComponent,
    ProductComponent
  ],
  imports: [
    CommonModule,
    CatalogRoutingModule,
    TableModule,
    ToastModule,
    ToolbarModule,
    DialogModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    InputTextareaModule,
    DropdownModule,
    InputMaskModule,
    DropdownModule,
    FormsModule,
    ConfirmDialogModule,
    ReactiveFormsModule,
    SidebarModule,
    TabMenuModule,
    TabViewModule,
    FileUploadModule
]
})
export class CatalogModule { }
