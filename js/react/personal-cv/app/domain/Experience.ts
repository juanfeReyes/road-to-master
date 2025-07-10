
interface JobRole {
  name: string,
  startDate: Date,
  icon?: string
}

interface Experience {
  company: string,
  startDate: Date,
  roles: JobRole[]
}
