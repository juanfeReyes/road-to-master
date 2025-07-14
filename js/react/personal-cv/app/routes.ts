import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
  route('/road-to-master',"routes/home.tsx"),
  route("*?", "routes/catchall.tsx"),
] satisfies RouteConfig;
