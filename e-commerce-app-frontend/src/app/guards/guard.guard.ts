import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, ActivatedRoute } from '@angular/router';
import { UtilsService } from '../utils/utils.service';
import { Constants } from '../interfaces/general/constants';

@Injectable({
  providedIn: 'root'
})
export class GuardGuard implements CanActivate {

  sessionedUserData: any;

  constructor(
    private utilService     : UtilsService,
    private router          : Router) 
    {
      this.sessionedUserData = JSON.parse(localStorage.getItem('_user_data') || '{}');
    }

  canActivate(
    route: ActivatedRouteSnapshot, 
    state: RouterStateSnapshot): boolean {

    // Clear session storage if navigating to auth/login
    if (state.url === '/auth/login') {
      this.utilService.clearSessionStorage();
      return true;
    }

    // Validation of active account before accessing the route /
    const isValidAccount = this.utilService.getSessionStorageItem(Constants.SESSION_VAL_TYPES.IS_VALID_ACCOUNT);
    if (isValidAccount === "false" || isValidAccount === false) {
      this.utilService.clearSessionStorage();      
      return false;
    } 

    const userRoles = this.sessionedUserData?.roles||[];
    const requiredRoles = //roles required to access the route
          route.data?.['roles'] as Array<string> || [];    
    if (requiredRoles && !this.hasRequiredRole(userRoles, requiredRoles)) {
      this.router.navigate(['/']);
      return false;
    }

    return true;
  }

  private hasRequiredRole(userRoles: any, requiredRoles: string[]): boolean {
    
    if (!userRoles) {
      return false;
    }

    for (let i = 0; i < userRoles.length; i++) {
      return requiredRoles.includes(userRoles[i].name); 
    }
    return false;
  }
}
