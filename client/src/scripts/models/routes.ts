type HomeRoute = '/'
type Platforms = 'spotify' | 'deezer' | HomeRoute

export interface AppRoutes {
  platform: Platforms
}