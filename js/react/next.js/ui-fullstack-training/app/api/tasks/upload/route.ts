import { NextRequest, NextResponse } from "next/server";
import path from "path";
import fs from "fs";
import { error } from "console";

const validExtensions = ['.jpeg', 'jpg', 'pdf']

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

    const fileName = files.map(file => file.name);
    console.log(`File names ${fileName}`)
    const invalidFileNames = files
      .filter(file => validExtensions.includes(path.extname(file.name)))
      .map(file => file.name);

    if(invalidFileNames.length > 0) {
      return NextResponse.json(
        {error: `Invalid extension files ${invalidFileNames} must be ${validExtensions}`},
        {status: 400}
      )
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