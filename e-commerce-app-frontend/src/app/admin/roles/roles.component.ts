import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { Role } from 'src/app/interfaces/users/role';
import { RolesService } from './roles.service';
import { Response } from 'src/app/interfaces/general/response';
import { Constants } from 'src/app/interfaces/general/constants';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { HttpStatusCode } from '@angular/common/http';
import { UtilsService } from 'src/app/utils/utils.service';

@Component({
    templateUrl: './roles.component.html',
    providers: [MessageService],
    standalone: false
})
export class RolesComponent implements OnInit {

    roleDialog: boolean = false;

    deleteRoleDialog: boolean = false;

    //roles
    roles: Role[] = [];

    //One role
    role: Role = {};

    selectedRoles: Role[] = [];

    submitted: boolean = false;

    cols: any[] = [];

    rowsPerPageOptions = [5, 10, 20];

    //Response
    response : Response<Role[]>;

    loading: boolean = false;

    constructor(
        private rolService          : RolesService, 
        private sharedMessageService: SharedMessageService,
        private utilService         : UtilsService
    ) { }

    ngOnInit() {        
        this.getRoles();
    }

    getRoles(){
        this.role.status = 'active';
        this.loading     = true;
        this.rolService.getRoles(this.role).subscribe({
            next: (data) => {
                this.response = data;         
                this.loading  = false; 
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.roles = this.response.payload;               
                }
            },
            error: (error) => {    
                this.loading = false;                  
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting roles', error);            
            }
        });
    }

    openNew() {
        this.role = {};
        this.submitted = false;
        this.roleDialog = true;
    }

    editRole(role: Role) {
        this.role = { ...role };
        this.roleDialog = true;
    }

    deleteRole(role: Role) {
        this.deleteRoleDialog = true;
        this.role = { ...role };
    }

    confirmDelete() {
        this.deleteRoleDialog = false;
        this.rolService.deleteRole(this.role.id).subscribe({
            next: (data) => {
                this.response = data;          
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Role deleted successfully");            
                    this.getRoles();           
                } else {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Role', this.response.message);                                    
                }                    
            },
            error: (error) => {                  
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting role', error);                                    
            }
        });
        this.role = {};
    }

    hideDialog() {
        this.roleDialog = false;
        this.deleteRoleDialog = false;
        this.submitted = false;
    }

    saveRole() {
        
        this.submitted = true;
        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.INFO, 'Info', "Role is been created or updated",0);            

        if (this.role.name?.trim()) 
        {
            if (this.role.id) 
            {                
                this.rolService.updateRole(this.role.id,this.role).subscribe({
                    next: (data) => {
                        this.response = data;          
                        if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') 
                        {
                            this.sharedMessageService.clearMessages();
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Role updated");            
                            this.getRoles();           
                        } 
                        else 
                        {
                            this.sharedMessageService.clearMessages();
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating Role', this.response.message);                                    
                        }                    
                    },
                    error: (error) => {  
                       this.sharedMessageService.clearMessages();
                       this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating role', error);                                    
                    }
                });    
            } else {
                this.rolService.createRole(this.role).subscribe({
                    next: (data) => {
                        this.response = data;          
                        if (this.response.code === HttpStatusCode.Created && this.response.status === 'OK') 
                        {
                            this.sharedMessageService.clearMessages();
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Role created");                                    
                            this.getRoles();           
                        } 
                        else 
                        {
                            this.sharedMessageService.clearMessages();
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new Role', this.response.message);                                    
                        }                    
                    },
                    error: (error) => {  
                        this.sharedMessageService.clearMessages();
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new Role', error);                                    
                    }
                });               
            }

            this.roleDialog = false;
            this.role       = {};
        }
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }   
}
