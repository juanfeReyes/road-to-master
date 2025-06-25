import {
  type RouteConfig,
  index,
  layout,
  prefix,
  route,
} from "@react-router/dev/routes";

const routersFolder = './components/routers'

export default [
  index(`${routersFolder}/Home.tsx`),

  // outlet without prefix
  layout(`${routersFolder}/auth/Layout.tsx`, [
    route('login', `${routersFolder}/auth/Login.tsx`),
    route('register', `${routersFolder}/auth/Register.tsx`)
  ]),

  //prefix without parent outlet
  ...prefix('homes', [
    index(`${routersFolder}/homes/Index.tsx`),
    route(':homeId', `${routersFolder}/homes/HomeDetail.tsx`),
    route('form/:id?', `${routersFolder}/homes/HomeForm.tsx`),
  ]),

  // prefix with outlet
  route('reports', `${routersFolder}/reports/Dashboard.tsx`, [
    index(`${routersFolder}/reports/Index.tsx`),
    route('homes', `${routersFolder}/reports/Homes.tsx`),
    route('offers', `${routersFolder}/reports/Offers.tsx`)
  ]),

  // * matches all URLs, the ? makes it optional so it will match / as well
  route("*?", "catchall.tsx"),
] satisfies RouteConfig;

