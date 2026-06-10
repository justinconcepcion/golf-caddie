// ── Enums ────────────────────────────────────────────────────────────────────

export type Club =
  | 'DRIVER'
  | 'THREE_WOOD'
  | 'FIVE_WOOD'
  | 'HYBRID'
  | 'FOUR_IRON'
  | 'FIVE_IRON'
  | 'SIX_IRON'
  | 'SEVEN_IRON'
  | 'EIGHT_IRON'
  | 'NINE_IRON'
  | 'PITCHING_WEDGE'
  | 'GAP_WEDGE'
  | 'SAND_WEDGE'
  | 'LOB_WEDGE'
  | 'PUTTER'

export type Handedness = 'RIGHT' | 'LEFT'

export type LieType = 'TEE' | 'FAIRWAY' | 'ROUGH' | 'BUNKER' | 'GREEN' | 'UNKNOWN'

// ── Profile ──────────────────────────────────────────────────────────────────

export interface PlayerProfileResponse {
  id: number
  handedness: Handedness
  clubDistancesYards: Partial<Record<Club, number>>
}

export interface UpdateProfileRequest {
  handedness: Handedness
  clubDistancesYards: Partial<Record<Club, number>>
}

// ── Rounds & Shots ────────────────────────────────────────────────────────────

export interface CreateRoundRequest {
  courseId: string
}

export interface RoundResponse {
  id: number
  courseId: string
  createdAt: string
}

export interface LogShotRequest {
  holeNumber: number
  lat: number
  lng: number
}

export interface ShotResponse {
  id: number
  holeNumber: number
  shotNumber: number
  lat: number
  lng: number
  lie: LieType
  distanceToPinYards: number
}

// ── Advice ────────────────────────────────────────────────────────────────────

export interface AdviceRequest {
  courseId: string
  holeNumber: number
  lat: number
  lng: number
  shotNumber?: number
}

export interface AdviceResponse {
  holeNumber: number
  lie: LieType
  distanceToPinYards: number
  advice: string
}
