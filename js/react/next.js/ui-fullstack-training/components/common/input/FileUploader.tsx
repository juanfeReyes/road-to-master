import { Icon } from "@iconify/react"
import { useState, DragEvent, ChangeEvent } from "react"

export const FileUploader = () => {
    const acceptedFiles = ['.jpg', '.png', '.pdf', '.docx']
    const [files, setFiles] = useState<File[]>([])

    const handleFileRemoval = (idx: number) => {
        const newFiles = files.toSpliced(idx, 1)
        console.log(newFiles)
        setFiles(newFiles)
    }

    const handleDragOver = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault()
    }

    const handleDrops = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        const droppedFiles = Array.from(e.dataTransfer.files)
        setFiles((currentFiles) => [...currentFiles, ...droppedFiles])
    }

    const handleOnFileSelectChange = (e: ChangeEvent<HTMLInputElement, HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setFiles((currentFiles) => [...currentFiles, ...Array.from(e.target.files)])
        }
    }

    return (<div
        className="flex gap-3 border-2 rounded-2xl border-dashed p-1 justify-center items-center"
        onDrop={handleDrops}
        onDragOver={handleDragOver}
    >
        <div>
            <label
                for="file-uploader"
                className="text-center"
            >Drop or select files</label>
            <input
                className="hidden"
                id="file-uploader"
                type="file"
                onChange={handleOnFileSelectChange}
                accept={acceptedFiles.join(',')}
                multiple
            />
        </div>
        <ul>
            {files.map((file, idx) => (<div
                onClick={() => handleFileRemoval(idx)}
                className="flex gap-2 flex-col  items-center">
                <Icon icon={"mdi:file"} className="text-2xl" />
                <li
                    key={`${file.name}-${idx}`}
                    className="w-20 truncate"
                >{file.name}</li>
            </div>
            ))}
        </ul>
    </div>)

}
