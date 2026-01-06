export interface AuthorDetails {
  authorName: string;
  authorEmail: string;
}

export interface Blog {
  postId: string;
  blogName: string;
  article: string;
  category: string;
  tagIds?: string[];
  createdAt: string;
  updatedAt?: string;
  wordCount?: number;
  author: AuthorDetails;
}

export interface PagedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp?: string;
  path?: string;
}
