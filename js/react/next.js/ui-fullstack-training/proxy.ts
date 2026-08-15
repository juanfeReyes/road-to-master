import { NextRequest, NextResponse } from "next/server";
import { verifySession } from "./lib/dataAccessLayer/users";

export async function proxy(request: NextRequest) {
    const session = await verifySession()

    if (!session) {
      // Always use absolute URLs for responses
      return NextResponse.redirect(new URL('/login', request.url))
    }

    return NextResponse.next();
}

export const config = {
  matcher: ["/tasks"], // Specify the routes the middleware applies to
};