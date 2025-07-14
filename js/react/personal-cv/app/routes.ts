import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
  route('road-to-master',"routes/home.tsx"), //TODO: update to only add repo when build for GH
  route("*?", "routes/catchall.tsx"),
] satisfies RouteConfig;
