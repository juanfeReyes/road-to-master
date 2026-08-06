import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { NotificationToast } from "@/components/common/interactivity/NotificationToast";
import { CustomClientProvider } from "@/components/common/CustomClientProvider";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});


export const metadata: Metadata = {
  title: "TaskMan",
  description: "Manage Task and keep track your progress",
};

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {

  return (
    <html
      lang="en"
      className={`${geistSans.variable} ${geistMono.variable} h-full antialiased`}
    >
      <body className="min-h-full flex flex-col">
        <CustomClientProvider>
          <NotificationToast>
            {children}
          </NotificationToast>
        </CustomClientProvider>
      </body>
    </html>
  );
}
