# Blog UI - Category Search

Angular application for searching and viewing blogs by category.

## Features

- **Basic Search**: Search blogs by category name using a text input
- **Advanced Filter**: 
  - Select category from dropdown (includes "All Categories" option)
  - Filter by date range (start date and end date)
  - Clear filters button to reset all selections and results
- **Author Details**: Display author name and email for each blog
- **Pagination**: Navigate through multiple pages of results
- **Responsive Design**: Works on desktop and mobile devices

## Prerequisites

- Node.js (v18 or higher)
- npm (v9 or higher)
- Angular CLI (v17 or higher)

## Installation

1. Navigate to the blog-ui directory:
```bash
cd blog-ui
```

2. Install dependencies:
```bash
npm install
```

## Configuration

Update the API URL in `src/environments/environment.ts` if your backend service is running on a different port:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8083' // blog-query-service port
};
```

## Running the Application

1. Start the development server:
```bash
npm start
```

2. Open your browser and navigate to:
```
http://localhost:4200
```

## Usage

### Basic Search
1. Enter a category name in the text box (e.g., "Technology", "Science")
2. Click the "Search" button or press Enter
3. View the list of blogs with author details

### Advanced Filter
1. Click "Advanced Filter" button
2. Select a category from the dropdown (or "All Categories" for all)
3. Select a start date and end date
4. Click "Search"
5. View filtered results

### Clear Filters
- Click "Clear Filters" or "Clear All" to reset all search criteria and results

## API Endpoints Used

- `GET /api/v1.0/blogsite/blogs/info/{category}` - Get blogs by category
- `GET /api/v1.0/blogsite/blogs/get/{category}/{startDate}/{endDate}` - Search blogs by category and date range
- `GET /api/v1.0/blogsite/blogs/categories` - Get all available categories

## Project Structure

```
blog-ui/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   └── category-search/     # Main search component
│   │   ├── models/
│   │   │   └── blog.model.ts        # TypeScript interfaces
│   │   ├── services/
│   │   │   └── blog.service.ts       # API service
│   │   ├── app.component.ts         # Root component
│   │   └── app.route.ts             # Routing configuration
│   ├── environments/
│   │   ├── environment.ts           # Development config
│   │   └── environment.prod.ts      # Production config
│   └── main.ts                      # Application bootstrap
├── angular.json                     # Angular configuration
├── package.json                     # Dependencies
└── tsconfig.json                    # TypeScript configuration
```

## Build for Production

```bash
npm run build
```

The build artifacts will be stored in the `dist/blog-ui` directory.

## Troubleshooting

1. **CORS Issues**: Ensure your backend service allows requests from `http://localhost:4200`
2. **API Connection**: Verify that the blog-query-service is running on port 8083
3. **No Results**: Check that blogs exist in the database with the selected category
