export interface AnalyticsDashboard {

  totalSearches: number;

  totalTravelers: number;

  popularRoutes: {
    [key: string]: number;
  };
}