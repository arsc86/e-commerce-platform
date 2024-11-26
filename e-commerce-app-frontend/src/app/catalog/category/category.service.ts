import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Category } from 'src/app/interfaces/catalog/category';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';
import { Response } from 'src/app/interfaces/general/response';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private apiUrl: string;
  private categoryPath = '/catalog/category';

  constructor(
    private http   : HttpClient,
    private config : ConfigService,
    private utils  : UtilsService
    ) 
    { 
      this.apiUrl = this.config.API_URL+this.categoryPath;
    }

    getCategories(category : Category): Observable<Response<Category[]>> {
      let params = new HttpParams();
      if (category) 
      {
        if(category.status)
        {
          params = params.set('status', category.status);     
        }     
        if(category.providerCode)
        {
          params = params.set('providerCode', category.providerCode);
        }
    
      }
      return this.http.get<Response<Category[]>>(this.apiUrl, { params: params }).pipe(
        catchError(this.utils.handleError)
      );
    }

    createCategory(category: Category): Observable<Response<Category[]>> {    
      return this.http.post<Response<Category[]>>(this.apiUrl, category).pipe(
        catchError(this.utils.handleError)
      );
    }
    
    updateCategory(id: string, category: Category): Observable<Response<Category[]>> {
      return this.http.patch<Response<Category[]>>(`${this.apiUrl}/${id}`, category).pipe(
        catchError(this.utils.handleError)
      );
    }

    deleteCategory(id: string): Observable<Response<Category[]>> {
      return this.http.delete<Response<Category[]>>(`${this.apiUrl}/${id}`).pipe(
        catchError(this.utils.handleError)
      );
    }
  
}
