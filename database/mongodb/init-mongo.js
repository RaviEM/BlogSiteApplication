//:
// BLOG SITE APPLICATION - MongoDB Schema
// Database: blogsite_blogs
// Collections: blog_posts, categories, comments, tags, likes, notifications // ===
// Switch to the blog database
db = db.getSiblingDB('blogsite_blogs');
// ==
// BLOG POSTS COLLECTION
// ===
db.createCollection('blog_posts', {
	validator: {
		$jsonSchema: {
			bsonType: 'object',
			required: ['title', 'content', 'category_id', 'author_id', 'content', 'category_id', 'author_id', 'author_name', 'created_at'],
			properties: {
				_id: {
					bsonType: 'objectId',
					description: 'Unique identifier for the blog post'
				},
				title: {
					bsonType: 'string',
					minLength: 20,
					description: 'Blog post title minimum 20 characters
				},
				content: {
					bsonType: 'string',
					description: 'Blog post content/article should have minimum 1000 words'
				},
				category_id: {
					bsonType: 'string',
					description: 'Reference to category document'
				},
				author_id: {
					bsonType: 'long',
					description: 'Reference to user ID in MySQL
				},
				author_name: {
					bsonType: 'string',
					description: 'Author display name'
				},
				comment_ids: {
					bsonType: 'array',
					items: {
						bsonType: 'string'
					},
					description: 'Array of comment IDs'
				},
				tag_ids: {
					bsonType: 'array',
					items: {
						bsonType: 'string'
					},
					description: 'Array of tag IDs'
				},
				like_ids: {
					bsonType: 'array',
					items: {
						bsonType: 'string'
					},
					description: 'Array of like IDs'
				},
				is_published: {
					bsonType: 'bool',
					description: 'Publication status'
				},
				view_count: {
					bsonType: 'long',
					minimum: 0,
					description: 'Number of views'
				},
				like_count: {
					bsonType: 'long',
					minimum: 0,
					description: 'Number of Likes'
				},
				comment_count: {
					bsonType: 'long',
					minimum: 0,
					description: 'Number of comments
				},
				created_at: {
					bsonType: 'date',
					description: 'Creation timestamp"
				},
				updated_at: {
					bsonType: 'date',
					description: 'Last update timestamp"
				}
			}
		}
	},
	validationLevel: 'moderate',
	validationAction: 'warn'
});
// Indexes for blog_posts

db.blog_posts.createIndex({
			'title': 'text '
			content ': '
			text ' }, { name: '
			idx_blog_posts_text_search ' });
			db.blog_posts.createIndex({
					'category_id": 1 }, { name: '
					idx_blog_posts_category ' });
					db.blog_posts.createIndex({
							'author_id": 1 }, { name: '
							idx_blog_posts_author ' });
							db.blog_posts.createIndex({
								'created_at' - 1
							}, {
								name: 'idx_blog_posts_created_at'
							});
							db.blog_posts.createIndex({
								'is_published': 1,
								'created_at': -1
							}, {
								name: 'idx_blog_posts_published'
							});
							db.blog_posts.createIndex({
								'tag_ids': 1
							}, {
								name: 'idx_blog_posts_tags'
							});
							db.blog_posts.createIndex({
								'view_count': 1
							}, {
								name: 'idx_blog_posts_popular'
							});
							print('Created blog_posts collection with indexes');
							// ==
							// CATEGORIES COLLECTION
							// ====
							db.createCollection('categories', {
									validator: {
										$jsonSchema: {
											bsonType: 'object',
											required: ['name', 'created_at'],
											properties: {
												_id: {
													bsonType: 'objectId',
													description: 'Unique identifier for the category'
												},
												name: {
													bsonType: 'string',
													minLength: 20,
													description: 'Category name minimum 20 characters'
												},
												description: {
													bsonType: 'string',
													description: 'Category description'
												},
												post_ids: {
													bsonType: 'array',
													items: bsonType: 'string'
												},
												description: 'Array of post IDs in this category'
											},
											post_count: {
												bsonType: 'long',
												minimum: 0,
												description: 'Number of posts in category'
											},
											is_active: {
												bsonType: 'bool',
												description: 'Category active status'
											},
											created_at: {
												bsonType: 'date',
												description: 'Creation timestamp'
											},
											updated at: {
												bsonType: 'date',
												description: 'Last update timestamp
											}
										}
									}
								},
								validationLevel: 'moderate',
								validationAction: 'warn'
							});
						// Indexes for categories
						db.categories.createIndex({
							'name': 1
						}, {
							unique: true,
							name: 'idx_categories_name_unique'
						}); db.categories.createIndex({
								'is_active": 1 }, { name: '
								idx_categories_active ' });
								db.categories.createIndex({
									'post_count': -1
								}, {
									name: 'idx_categories_post_count'
								});
								print('Created categories collection with indexes');
								// COMMENTS COLLECTION
								// ==
								db.createCollection('comments', {
									validator: {
										$jsonSchema: {
											bsonType: 'object',
											required: ['content', 'author_id', 'post_id', 'created_at'],
											properties: {
												_id: {
													bsonType: 'objectId',
													description: 'Unique identifier for the comment'
												},
												content: {
													bsonType: 'string',
													description: 'Comment content'
												},
												author_id: {
													bsonType: 'long',
													description: 'Reference to user ID in MySQL'
												},
												author_name: {
													bsonType: 'string',
													description: 'Author display name'
												},
												post_id: {
													bsonType: 'string',
													description: 'Reference to blog post'
												},
												parent_comment_id: {
													bsonType: ['string', 'null'],
													description: 'Parent comment ID for replies'
												},
												timestamp: {
													bsonType: 'date',
													description: 'Comment timestamp'
												},
												is_active: {
													bsonType: 'bool',
													description: 'Comment active status'
												},
												created_at: {
													bsonType: 'date',
													description: 'Creation timestamp"
												},
												updated_at: {
													bsonType: 'date',
													description: 'Last update timestamp"
												},
											}
										}
									},
									validationLevel: 'moderate',
									validationAction: 'warn'
								});



								// Indexes for comments
								db.comments.createIndex({
									'post_id': 1,
									'created_at' - 1
								}, {
									name: 'idx_comments_post_date'
								});
								db.comments.createIndex({
									'author_id': 1
								}, {
									name: 'idx_comments_author'
								});
								db.comments.createIndex({
									'parent_comment_id': 1
								}, {
									name: 'idx_comments_parent'
								});
								db.comments.createIndex({
									'is_active': 1
								}, {
									name: 'idx_comments_active'
								});
								print('Created comments collection with indexes');
								// :
								// TAGS COLLECTION
								// ==
								db.createCollection('tags', {
									validator: {
										$jsonSchema: {
											bsonType: 'object',
											required: ['name', 'created_at'],
											properties: {
												_id: {
													bsonType: 'objectId',
													description: 'Unique identifier for the tag'
												},
												name: {
													bsonType: 'string',
													description: 'Tag name
												},
												post_ids: {
													bsonType: 'array',
													items: {
														bsonType: 'string'
													},
													description: 'Array of post IDs with this tag'
												},
												post_count: {
													bsonType: 'long',
													minimum: 0,
													description: 'Number of posts with this tag'
												},
												created_at: {
													bsonType: 'date',
													description: 'Creation timestamp"
												},
												updated_at: {
													bsonType: 'date',
													description: 'Last update timestamp
												}
											}
										}
									},
									validationAction: 'warn',
									validationLevel: 'moderate'
								});
								// Indexes for tags
								db.tags.createIndex({
									'name': 1
								}, {
									unique: true,
									name: 'idx_tags_name_unique'
								});
								db.tags.createIndex({
									'post_count': -1
								}, {
									name: 'idx_tags_popular'
								});
								print('Created tags collection with indexes');
								// ====
								// LIKES COLLECTION
								// ==
								db.createCollection('likes', {
									validator: {
										$jsonSchema: {
											bsonType: 'object',
											required: ['user_id', 'post_id', 'created_at'],
											properties: {
												_id: {
													bsonType: 'objectId',
													description: 'Unique identifier for the like'
												},
												user_id: {
													bsonType: 'long',
													description: 'Reference to user ID in MySQL'
												},
												post_id: {
													bsonType: 'string',
													description: 'Reference to blog post'
												},
												created_at: {
													bsonType: 'date',
													description: 'Creation timestamp'

												}
											}
										}
									},
									validationAction: 'error',
									validationLevel: 'strict'
								});
								// Indexes for likes - compound unique index to prevent duplicate likes
								db.likes.createIndex({
									'user_id': 1,
									'post_id': 1
								}, {
									unique: true,
									name: 'idx_likes_user_post_unique'
								});
								db.likes.createIndex({
									'post_id': 1
								}, {
									name: 'idx_likes_post'
								});
								db.likes.createIndex({
										'user_id": 1 }, { name: '
										idx_likes_user ' });
										print('Created likes collection with indexes');
										//
										// NOTIFICATIONS COLLECTION
										// ==
										db.createCollection('notifications {
											validator: {
												$jsonSchema: {
													bsonType: 'object',
													required: ['user_id', 'message', 'notification_type', 'created_at'],
													properties: {
														_id: {
															bsonType: 'objectId',
															description: 'Unique identifier for the notification'
														},
														user_id: {
															bsonType: 'long',
															description: 'Reference to user ID in MySQL'
														},
														message: {
															bsonType: 'string',
															description: 'Notification message'
														},
														timestamp: {
															bsonType: 'date',
															description: 'Notification timestamp
														},
														is_read: {
															bsonType: 'bool',
															description: 'Read status'
														},
														notification_type: {
															enum: ['COMMENT', 'LIKE', 'FOLLOW'
																'MENTION', 'SYSTEM', 'NEW_POST'
															],
															description: 'Type of notification
														},
														reference_id: {
															bsonType: ['string', 'null'],
															description: 'Reference to related entity'
															reference_type: {
																bsonType: ['string', 'null'],
																description: 'Type of referenced entity'
															},
															created_at: {
																bsonType: 'date',
																description: 'Creation timestamp"
															}
														}
													}
												},
												validation Level: 'moderate',
												validationAction: 'warn'
											});
										// Indexes for notifications
										db.notifications.createIndex({
											'user_id': 1,
											'created_at': -1
										}, {
											name: 'idx_notifications_user_date'
										});
										db.notifications.createIndex({
												'user_id': 1,
												'is_read: 1 }, { name: '
												idx_notifications_user_unread ' });
												db.notifications.createIndex({
													'notification_type': 1
												}, {
													name: 'idx_notifications_type'
												});
												// TTL index to auto-delete old notifications after 90 days
												db.notifications.createIndex({
													'created_at': 1
												}, {
													expireAfterSeconds: 7776000,
													name: 'idx_notifications_ttl'
												});
												print('Created notifications collection with indexes');
												//
												// UTILITY FUNCTIONS
												// Function to get blog posts by category with pagination
												// Usage: db.blog_posts.find({ category_id: 'categoryId', is_published: true }).sort({ created_at: -1 }).skip (0).limit(10) // Function to get blogs by date range
												// Usage: db.blog_posts.find({ created_at: { $gte: ISODate('2024-01-01'), $lte: ISODate('2024-12-31') } })
												// Function to search blogs
												// Usage: db.blog_posts.find({ $text: { $search: 'search term' } })
												//
												// SAMPLE DATA (Optional - for testing)
												//
												// Sample category
												db.categories.insertOne({

													name: 'Technology and Innovation',
													description: 'Articles about latest technology trends and innovations',
													post_ids: [],
													post_count: NumberLong(0),
													is_active: true,
													created_at: new Date(),
													updated_at: new Date()
												});
												// Sample tag
												db.tags.insertOne({
													name: 'programming',
													post_ids: [],
													post_count: NumberLong(0),
													created_at: new Date(),
													updated_at: new Date()
												});

												print('MongoDB initialization completed successfully!');