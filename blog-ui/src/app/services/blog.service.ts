import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Blog, ApiResponse, PagedResponse } from '../models/blog.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  private apiUrl = `${environment.apiUrl}/api/v1.0/blogsite/blogs`;

  constructor(private http: HttpClient) {}

  /**
   * Get blogs by category
   */
  getBlogsByCategory(category: string, page: number = 0, size: number = 10): Observable<PagedResponse<Blog>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<ApiResponse<PagedResponse<Blog>>>(`${this.apiUrl}/info/${category}`, { params })
      .pipe(
        map(response => response.data)
      );
  }

  /**
   * Search blogs by category and date range
   */
  searchBlogsByCategoryAndDate(
    category: string, 
    startDate: string, 
    endDate: string, 
    page: number = 0, 
    size: number = 10
  ): Observable<PagedResponse<Blog>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<ApiResponse<PagedResponse<Blog>>>(
      `${this.apiUrl}/get/${category}/${startDate}/${endDate}`, 
      { params }
    ).pipe(
      map(response => response.data)
    );
  }

  /**
   * Get all categories
   */
  getAllCategories(): Observable<string[]> {
    return this.http.get<ApiResponse<string[]>>(`${this.apiUrl}/categories`)
      .pipe(
        map(response => response.data)
      );
  }

  /**
   * Get blog by ID
   */
  getBlogById(postId: string): Observable<Blog> {
    return this.http.get<ApiResponse<Blog>>(`${this.apiUrl}/${postId}`)
      .pipe(
        map(response => response.data)
      );
  }
}
