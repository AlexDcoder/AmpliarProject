export interface Patient {
  id: string
  name: string
  cpf: string
  phone: string
  email: string // ATUALIZADO: Agora é string obrigatória
  birthDate: string
  address: string // ATUALIZADO: Agora é string obrigatória
  status: "active" | "inactive"
  lastAppointment?: string
  totalAppointments: number
  legalGuardianIds: string[]
  notes: string // ATUALIZADO: Agora é string obrigatória
  createdAt: string
  updatedAt: string
}

export interface ResponsibleParty {
  id: string
  name: string
  cpf: string
  phone: string
  email: string
  relationship: string
  patients: string[]
}
