import { tasksDB } from "@/lib/mocks/TasksDB"
import { NextResponse } from "next/server";
import path from "path";
import fs from "fs";

const validExtensions = ['.jpeg', 'jpg', 'pdf']

export async function GET(request: Request) {

    const db = tasksDB.all()
    return NextResponse.json(db)
}

export async function POST(request: Request) {
    try {
        const formData = await request.formData();

        const files = formData.getAll("files") as File[] | null;
        const taskString = formData.get("task") as string | null

        if (!taskString) {
            return NextResponse.json(
                { error: "No Task uploaded." },
                { status: 400 }
            );
        }

        if (files) {
            const fileName = files.map(file => file.name);
            const invalidFileNames = files
                .filter(file => !validExtensions.includes(path.extname(file.name)))
                .map(file => file.name);

            if (invalidFileNames.length > 0) {
                return NextResponse.json(
                    { error: `Invalid extension files ${invalidFileNames} must be ${validExtensions}` },
                    { status: 400 }
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
        }
        const tasRequest = JSON.parse(taskString)
        tasRequest.id = crypto.randomUUID();
        const createdTask = await tasksDB.create(tasRequest)
        return Response.json(createdTask)
    } catch (error) {
        console.error("Upload error:", error);
        return NextResponse.json(
            { error: "Internal Server Error" },
            { status: 500 }
        );
    }
}


