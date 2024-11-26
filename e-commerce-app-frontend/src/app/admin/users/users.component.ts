import { Component, OnInit } from '@angular/core';
import { MessageService, SelectItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { Role } from 'src/app/interfaces/users/role';
import { Response } from 'src/app/interfaces/general/response';
import { Constants } from 'src/app/interfaces/general/constants';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { HttpStatusCode } from '@angular/common/http';
import { Provider } from 'src/app/interfaces/catalog/provider';
import { UserService } from './user.service';
import { User } from 'src/app/interfaces/users/user';
import { UserUtilService } from 'src/app/interfaces/users/user-util.service';
import { RolesService } from '../roles/roles.service';
import { ProviderService } from '../provider/provider.service';

@Component({
    templateUrl: './users.component.html',
    providers: [MessageService],
    standalone: false
})
export class UsersComponent implements OnInit {

    //One user
    user: User = this.userUtil.initializeUser();

    userDialog: boolean = false;

    deleteUserDialog: boolean = false;

    //users
    users: User[] = [];

    selectedUsers: User[] = [];

    submitted: boolean = false;

    cols: any[] = [];

    rowsPerPageOptions = [5, 10, 20];

    //Response
    response : Response<Role[]>;

    loading: boolean = false;

    userTypes: SelectItem[] = [];

    filteredUserTypes: SelectItem[] = [];

    isNewUser: boolean = false;

    //Provider

    providerResponse: Response<Provider[]>;

    providerTypes: SelectItem[] = [];

    constructor(
        private userService         : UserService, 
        private sharedMessageService: SharedMessageService,
        private userUtil            : UserUtilService,
        private roleService         : RolesService,
        private providerService     : ProviderService
    ) { }

    ngOnInit() {     
        this.isNewUser = false;   
        this.getUsers();
        this.roleService.getRoles(null).subscribe({
            next: (data) => {
                this.response = data;         
                this.loading  = false; 
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    let roles = this.response.payload;  
                    const userTypeOptions: SelectItem[] = roles.map(role => ({
                        label: role.name.toUpperCase(),
                        value: role.name.toUpperCase()
                    }));             
                     this.filteredUserTypes = [...userTypeOptions];
                    this.userTypes = userTypeOptions.filter(type => type.value !== 'USER');
                    console.log(this.userTypes);
                }
            },
            error: (error) => {    
                this.loading = false;                  
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting roles', error);            
            }
        });

        let provider: Provider = {};
        provider.status = 'active';
        this.providerService.getProviders(provider).subscribe({
            next: (data) => {
                this.response = data; 
                this.loading       = false;
                
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    
                    this.providerResponse = data;     
                    this.providerTypes = this.providerResponse.payload.map(provider => ({                        
                        label: provider.name,
                        value: provider.providerCode
                    }));
                   console.log(this.providerTypes);
                }
            },
            error: (error) => {    
                this.loading = false;                  
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting providers', error);            
            }
        });


    }

    getUsers(userFilter: User | null = null) {
        if (userFilter) {
            this.user = userFilter;
        }
        this.user.status = 'active';
        this.loading     = true;
        this.userService.getUsers(this.user).subscribe({
            next: (data) => {
                this.response = data;         
                this.loading  = false; 
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.users = this.response.payload;                                
                }
            },
            error: (error) => {    
                this.loading = false;                  
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting users', error);            
            }
        });
    }

    openNew() {
        this.user = this.userUtil.initializeUser();
        this.submitted = false;
        this.userDialog = true;
        this.isNewUser = true;
    }

    editUser(user: User) {
        
        if (user.roles && user.roles.length > 0) {
            user.role = user.roles[0].name;
        }
        this.user = { ...user };
        this.userDialog = true;
        this.isNewUser = false;
    }

    deleteUser(user: User) {       
        this.deleteUserDialog = true;
        this.user = { ...user };
    }

    confirmDelete() {
        this.deleteUserDialog = false;
        this.userService.deleteUser(this.user.id).subscribe({
            next: (data) => {
                this.response = data;          
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "User deleted successfully");            
                    this.getUsers();           
                } else {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting User', this.response.message);                                    
                }                    
            },
            error: (error) => {                  
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting User', error);                                    
            }
        });
        this.user = {};
    }

    hideDialog() {
        this.userDialog = false;
        this.deleteUserDialog = false;
        this.submitted = false;
    }

    saveUser() {

        if (
            !this.user.firstName ||
            !this.user.lastName ||
            !this.user.username ||
            !this.user.profile?.email ||
            !this.user.role
        ) {
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Validation Error', 'All fields are required');
            return;
        }

        //Sending from admin new user option
        this.user.byAdmin = true;

        this.submitted = true;
        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.INFO, 'Info', "User is been created or updated",0);

        if (this.user.username?.trim())
        {
            if (this.user.id)
            {
                this.userService.updateUser(this.user.id,this.user).subscribe({
                    next: (data) => {
                        this.response = data;          
                        if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') 
                        {
                            this.sharedMessageService.clearMessages();
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "User updated");            
                            this.getUsers();   
                            this.userDialog = false;
                            this.user = {};        
                        } 
                        else 
                        {
                            this.sharedMessageService.clearMessages();
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating User', this.response.message);                                    
                        }                    
                    },
                    error: (error) => {  
                       this.sharedMessageService.clearMessages();
                       this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating User', error);                                    
                    }
                });    
            } else {
                this.userService.createUser(this.user).subscribe({
                    next: (data) => {
                        this.response = data;          
                        if (this.response.code === HttpStatusCode.Created && this.response.status === 'OK') 
                        {
                            this.sharedMessageService.clearMessages();
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "User created");                                    
                            this.getUsers();     
                            this.userDialog = false;
                            this.user = {};      
                        } 
                        else 
                        {
                            this.sharedMessageService.clearMessages();
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new User', this.response.message);                                    
                        }                    
                    },
                    error: (error) => {  
                        this.sharedMessageService.clearMessages();
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new User', error);                                    
                    }
                });               
            }

            
        }
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }   

    onRoleFilterChange(event: any) {
        const selectedRole = event.value;
        const user: User = this.userUtil.initializeUser();
        user.role = selectedRole;
        this.getUsers(user);
    }

    onRoleChange(event: any) {       
        const selectedRole = event.value;
        this.user.role = selectedRole;
        if (selectedRole === 'PROVIDER') {
            this.user.profile.providerCode = null; // Reset provider code if role is PROVIDER
        }
    }

}
