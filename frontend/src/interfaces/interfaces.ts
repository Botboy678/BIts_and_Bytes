export interface Profile {
  githubUrl: string;
  profilePhotoUrl: string;
  leetcodeUsername: string;
  leetcodeProblemsSolved: number;
  linkedinUrl: string;
}

export interface ProjectComment {
  user: UserSummary; // slimmer than full User
  content: string;
}

export interface Project {
  user: UserSummary;
  title: string;
  description: string;
  githubRepoUrl: string;
  createdAt: string;
  comments: ProjectComment[];
}
// For requests (what you send TO the backend)
export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface User {
  username: string;
  email: string;
  isOnline?: boolean;
  abilityPoints: number;
  registeredAt: string;
  lastVisitAt: string;
  profile: Profile;
  projects: Project[];
}

export interface UserSummary {
  id: number;
  username: string;
  profilePhotoUrl?: string;
}

export interface Friend {
  id: number;
  friendUsername: string;
  status: "PENDING" | "ACCEPTED" | "DECLINED "; 
  createdAt: string;
}

export interface DeveloperBlogComment {
  description: string;
  user: UserSummary;
  createdAt: string;
}

export interface DeveloperBlog {
  id: number;
  title: string;
  description: string;
  datePublished: string;
  author: UserSummary;
  comments: DeveloperBlogComment[];
}

export interface BugReport {
  id: number;
  user: UserSummary;
  description: string;
  createdAt: string;
  status: "open" | "in_progress" | "resolved ";
}
