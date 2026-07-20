import { NextRequest, NextResponse } from "next/server";
import path from "path";
import fs from "fs";

export async function POST(req: NextRequest) {
  try {
    const formData = await req.formData();
    
    const files = formData.getAll("files") as File[] | null;

    if (!files) {
      return NextResponse.json(
        { error: "No file uploaded." },
        { status: 400 }
      );
    }

    const uploadDir = path.join(process.cwd(), "public", "uploads");
    for (let i = 0; i < files.length; i++) {
        const file = files[i]
        const bytes = await file.arrayBuffer();
        const buffer = Buffer.from(bytes);
                
        if (!fs.existsSync(uploadDir)) {
            fs.mkdirSync(uploadDir, { recursive: true });
        }
        
        const filePath = path.join(uploadDir, file.name);
        console.log('path ->', filePath)
        
        fs.writeFileSync(filePath, buffer);
    }

    return NextResponse.json({
      success: true,
      message: `File saved successfully to ${uploadDir}`,
    });
  } catch (error) {
    console.error("Upload error:", error);
    return NextResponse.json(
      { error: "Internal Server Error" },
      { status: 500 }
    );
  }
}