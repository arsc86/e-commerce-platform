import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { UserUtilService } from '../interfaces/users/user-util.service';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html',
    standalone: false
})
export class AppMenuComponent implements OnInit {

    model: any[] = [];

    constructor(public layoutService: LayoutService,
               private utilUserService : UserUtilService
    ) {}

    ngOnInit() {
        let user = localStorage.getItem('_user_data');
        user = JSON.parse(user);
                                                               
            this.model = [
                {
                    label: 'Home',
                    items: [
                        { label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'] },
                        { label: 'Administration', icon: 'pi pi-fw pi-shield', visible: this.utilUserService.hasRole(user,['ADMIN']),
                            items: 
                            [
                                { label: 'Roles', icon: 'pi pi-fw pi-key', routerLink: ['/admin/roles']},
                                { label: 'Users', icon: 'pi pi-fw pi-user', routerLink: ['/admin/users']}                            
                            ] 
                        },
                        { label: 'Configuration', icon: 'pi pi-fw pi-cog', visible: this.utilUserService.hasRole(user,['ADMIN']),
                            items: 
                            [
                                { label: 'Provider', icon: 'pi pi-fw pi-briefcase', routerLink: ['/admin/provider'], visible: this.utilUserService.hasRole(user,['ADMIN'])},                           
                                { label: 'Region', icon: 'pi pi-fw pi-globe', routerLink: ['/admin/region'], visible: this.utilUserService.hasRole(user,['ADMIN'])},
                                { 
                                    label: 'Warehouse', 
                                    icon: 'pi pi-fw pi-bookmark', 
                                    routerLink: ['/admin/warehouse'],
                                    visible: this.utilUserService.hasRole(user,['ADMIN','PROVIDER'])
                                }
                            ] 
                        },
                        { label: 'Catalog', icon: 'pi pi-fw pi-book', visible: this.utilUserService.hasRole(user,['ADMIN','PROVIDER']),
                            items: 
                            [
                                { label: 'Category', icon: 'pi pi-fw pi-tags', routerLink: ['/catalog/category']},
                                { label: 'Characteristic', icon: 'pi pi-fw pi-list', routerLink: ['/catalog/characteristic']},
                                { label: 'Product', icon: 'pi pi-fw pi-box', routerLink: ['/catalog/product']}
                            ] 
                        }
                    ]
                },
                {
                    label: 'UI Components',
                    items: [
                        { label: 'Form Layout', icon: 'pi pi-fw pi-id-card', routerLink: ['/uikit/formlayout'] },
                        { label: 'Input', icon: 'pi pi-fw pi-check-square', routerLink: ['/uikit/input'] },
                        { label: 'Float Label', icon: 'pi pi-fw pi-bookmark', routerLink: ['/uikit/floatlabel'] },
                        { label: 'Invalid State', icon: 'pi pi-fw pi-exclamation-circle', routerLink: ['/uikit/invalidstate'] },
                        { label: 'Button', icon: 'pi pi-fw pi-box', routerLink: ['/uikit/button'] },
                        { label: 'Table', icon: 'pi pi-fw pi-table', routerLink: ['/uikit/table'] },
                        { label: 'List', icon: 'pi pi-fw pi-list', routerLink: ['/uikit/list'] },
                        { label: 'Tree', icon: 'pi pi-fw pi-share-alt', routerLink: ['/uikit/tree'] },
                        { label: 'Panel', icon: 'pi pi-fw pi-tablet', routerLink: ['/uikit/panel'] },
                        { label: 'Overlay', icon: 'pi pi-fw pi-clone', routerLink: ['/uikit/overlay'] },
                        { label: 'Media', icon: 'pi pi-fw pi-image', routerLink: ['/uikit/media'] },
                        { label: 'Menu', icon: 'pi pi-fw pi-bars', routerLink: ['/uikit/menu'], routerLinkActiveOptions: { paths: 'subset', queryParams: 'ignored', matrixParams: 'ignored', fragment: 'ignored' } },
                        { label: 'Message', icon: 'pi pi-fw pi-comment', routerLink: ['/uikit/message'] },
                        { label: 'File', icon: 'pi pi-fw pi-file', routerLink: ['/uikit/file'] },
                        { label: 'Chart', icon: 'pi pi-fw pi-chart-bar', routerLink: ['/uikit/charts'] },
                        { label: 'Misc', icon: 'pi pi-fw pi-circle', routerLink: ['/uikit/misc'] }
                    ]
                },                   
                {
                    label: 'Pages',
                    icon: 'pi pi-fw pi-briefcase',
                    items: [
                        {
                            label: 'Landing',
                            icon: 'pi pi-fw pi-globe',
                            routerLink: ['/landing']
                        },
                        {
                            label: 'Auth',
                            icon: 'pi pi-fw pi-user',
                            items: [
                                {
                                    label: 'Login',
                                    icon: 'pi pi-fw pi-sign-in',
                                    routerLink: ['/auth1/login']
                                },
                                {
                                    label: 'Error',
                                    icon: 'pi pi-fw pi-times-circle',
                                    routerLink: ['/auth/error']
                                },
                                {
                                    label: 'Access Denied',
                                    icon: 'pi pi-fw pi-lock',
                                    routerLink: ['/auth/access']
                                }
                            ]
                        },
                        {
                            label: 'Crud',
                            icon: 'pi pi-fw pi-pencil',
                            routerLink: ['/pages/crud']
                        },
                        {
                            label: 'Timeline',
                            icon: 'pi pi-fw pi-calendar',
                            routerLink: ['/pages/timeline']
                        },
                        {
                            label: 'Not Found',
                            icon: 'pi pi-fw pi-exclamation-circle',
                            routerLink: ['/notfound']
                        },
                        {
                            label: 'Empty',
                            icon: 'pi pi-fw pi-circle-off',
                            routerLink: ['/pages/empty']
                        },
                    ]
                },
                {
                    label: 'Hierarchy',
                    items: [
                        {
                            label: 'Submenu 1', icon: 'pi pi-fw pi-bookmark',
                            items: [
                                {
                                    label: 'Submenu 1.1', icon: 'pi pi-fw pi-bookmark',
                                    items: [
                                        { label: 'Submenu 1.1.1', icon: 'pi pi-fw pi-bookmark' },
                                        { label: 'Submenu 1.1.2', icon: 'pi pi-fw pi-bookmark' },
                                        { label: 'Submenu 1.1.3', icon: 'pi pi-fw pi-bookmark' },
                                    ]
                                },
                                {
                                    label: 'Submenu 1.2', icon: 'pi pi-fw pi-bookmark',
                                    items: [
                                        { label: 'Submenu 1.2.1', icon: 'pi pi-fw pi-bookmark' }
                                    ]
                                },
                            ]
                        },
                        {
                            label: 'Submenu 2', icon: 'pi pi-fw pi-bookmark',
                            items: [
                                {
                                    label: 'Submenu 2.1', icon: 'pi pi-fw pi-bookmark',
                                    items: [
                                        { label: 'Submenu 2.1.1', icon: 'pi pi-fw pi-bookmark' },
                                        { label: 'Submenu 2.1.2', icon: 'pi pi-fw pi-bookmark' },
                                    ]
                                },
                                {
                                    label: 'Submenu 2.2', icon: 'pi pi-fw pi-bookmark',
                                    items: [
                                        { label: 'Submenu 2.2.1', icon: 'pi pi-fw pi-bookmark' },
                                    ]
                                },
                            ]
                        }
                    ]
                }
            ];
    }

   
}
