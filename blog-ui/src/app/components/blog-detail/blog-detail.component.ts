import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { BlogService } from '../../services/blog.service';
import { Blog } from '../../models/blog.model';

@Component({
  selector: 'app-blog-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './blog-detail.component.html',
  styleUrls: ['./blog-detail.component.css']
})
export class BlogDetailComponent implements OnInit {
  blog: Blog | null = null;
  loading: boolean = false;
  error: string | null = null;
  postId: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private blogService: BlogService
  ) {}

  ngOnInit(): void {
    console.log('BlogDetailComponent initialized');
    this.route.params.subscribe(params => {
      this.postId = params['id'];
      console.log('Blog ID from route:', this.postId);
      if (this.postId) {
        this.loadBlogDetails();
      } else {
        console.error('No post ID found in route');
        this.error = 'Blog ID is missing';
      }
    });
  }

  /**
   * Load blog details by ID
   */
  loadBlogDetails(): void {
    this.loading = true;
    this.error = null;

    this.blogService.getBlogById(this.postId).subscribe({
      next: (blog) => {
        this.blog = blog;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading blog details:', err);
        this.error = err.error?.message || 'Failed to load blog details';
        this.loading = false;
      }
    });
  }

  /**
   * Navigate back to search results
   */
  goBack(): void {
    this.router.navigate(['/search']);
  }

  /**
   * Get formatted date for display
   */
  getFormattedDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
