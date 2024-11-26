import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Response } from 'src/app/interfaces/general/response';
import { Role } from 'src/app/interfaces/users/role';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';

@Injectable({
  providedIn: 'root'
})
export class RolesService {

  private apiUrl: string;
  private rolePath = '/user/role';

  constructor(
    private http   : HttpClient,
    private config : ConfigService,
    private utils  : UtilsService
  ) 
  { 
    this.apiUrl = this.config.API_URL+this.rolePath;
  }

  getRoles(role : Role): Observable<Response<Role[]>> {
    let params = new HttpParams();
    if (role) 
    {
      if(role.status)
      {
        params = params.set('status', role.status);
      }     
    }
    return this.http.get<Response<Role[]>>(this.apiUrl, { params: params }).pipe(
      catchError(this.utils.handleError)
    );
  }

  createRole(role: Role): Observable<Response<Role[]>> {
    return this.http.post<Response<Role[]>>(this.apiUrl, role).pipe(
      catchError(this.utils.handleError)
    );
  }
  
  updateRole(id: string, role: Role): Observable<Response<Role[]>> {
    return this.http.put<Response<Role[]>>(`${this.apiUrl}/${id}`, role).pipe(
      catchError(this.utils.handleError)
    );
  }
  
  deleteRole(id: string): Observable<Response<Role[]>> {
    return this.http.delete<Response<Role[]>>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.utils.handleError)
    );
  }

  

}
