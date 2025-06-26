import { ImSpinner } from "react-icons/im";
import {
  Links,
  Meta,
  Outlet,
  Scripts,
  ScrollRestoration,
  useNavigation,
} from "react-router";

export function HydrateFallback() {
  return <div>Loading...</div>;
}

export function Layout({
  children,
}: {
  children: React.ReactNode;
}) {
  const navigation = useNavigation()
  const isNavigating =  Boolean(navigation.location)


  return (
    <html lang="en">
      <head>
        <meta charSet="UTF-8" />
        <meta
          name="viewport"
          content="width=device-width, initial-scale=1.0"
        />
        <title>My App</title>
        <Meta />
        <Links />
      </head>
      <body>
        {isNavigating && <ImSpinner />}
        {children}
        <ScrollRestoration />
        <Scripts />
      </body>
    </html>
  );
}

export default function Root() {
  return <Outlet />;
}