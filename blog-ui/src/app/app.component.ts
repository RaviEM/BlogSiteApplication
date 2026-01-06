import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CategorySearchComponent } from './components/category-search/category-search.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CategorySearchComponent],
  template: `
    <div class="app-container">
      <header class="app-header">
        <h1>Blog Site</h1>
        <p>Discover and explore blogs by category</p>
      </header>
      <main class="app-main">
        <app-category-search></app-category-search>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    .app-header {
      background: rgba(255, 255, 255, 0.95);
      padding: 20px;
      text-align: center;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      margin-bottom: 20px;
    }
    .app-header h1 {
      margin: 0 0 10px 0;
      color: #333;
      font-size: 36px;
    }
    .app-header p {
      margin: 0;
      color: #666;
      font-size: 16px;
    }
    .app-main {
      padding: 20px 0;
    }
  `]
})
export class AppComponent {
  title = 'blog-ui';
}
