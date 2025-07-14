import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
  // route('/road-to-master',"routes/home.tsx"),//why not working
  route("*?", "routes/home.tsx"),
] satisfies RouteConfig;
