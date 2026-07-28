CREATE TYPE user_role as ENUM ('admin', 'user');
CREATE TYPE activity_status as ENUM ('ACTIVE', 'CLOSED');
CREATE TYPE disbursement_request_status as ENUM ('PENDING', 'APPROVED', 'REJECTED', 'RELEASED');
CREATE TYPE liquidation_status as ENUM ('DRAFT', 'SUBMITTED', 'APPROVED', 'REJECTED');
CREATE TYPE budget_reallocation_request_status as ENUM ('PENDING', 'APPROVED', 'REJECTED');
CREATE TYPE audit_action AS ENUM (
    'ACTIVITY_CREATED', 'ACTIVITY_UPDATED', 'ACTIVITY_CLOSED',
    'DISBURSEMENT_SUBMITTED', 'DISBURSEMENT_APPROVED', 'DISBURSEMENT_REJECTED', 'FUNDS_RELEASED',
    'LIQUIDATION_SUBMITTED', 'LIQUIDATION_APPROVED', 'LIQUIDATION_REJECTED',
    'BUDGET_REALLOCATION_REQUESTED', 'BUDGET_REALLOCATION_APPROVED', 'BUDGET_REALLOCATION_REJECTED', 'BUDGET_REALLOCATION_APPLIED',
    'SURPLUS_RETURNED'
);
CREATE TYPE audit_entity AS ENUM (
    'ACTIVITY', 'CATEGORY', 'DISBURSEMENT', 'LIQUIDATION', 'REALLOCATION', 'ACADEMIC_YEAR'
);
CREATE SEQUENCE seq_disbursement_request_count START 1;
CREATE SEQUENCE seq_reallocation_request_count START 1;


-- 2. Create Tables with Safety Constraints
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(150) NOT NULL,
    position VARCHAR(100) NOT NULL,
    password_hash TEXT NOT NULL,
    role user_role NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE academic_years (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    school_year VARCHAR(20) NOT NULL,
    total_budget NUMERIC(12,2) NOT NULL CHECK (total_budget >= 0),
    remaining_budget NUMERIC(12,2) NOT NULL CHECK (remaining_budget >= 0),
    created_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE activities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    academic_year_id UUID NOT NULL REFERENCES academic_years(id) ON DELETE RESTRICT,
    name VARCHAR(150) NOT NULL,
    type VARCHAR(30) NOT NULL,
    description TEXT,
    total_allocated NUMERIC(12,2) DEFAULT 0 CHECK (total_allocated >= 0),
    total_actual NUMERIC(12,2) DEFAULT 0 CHECK (total_actual >= 0),
    total_surplus NUMERIC(12,2) DEFAULT 0,
    status activity_status DEFAULT 'ACTIVE',
    created_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMPTZ
);

CREATE TABLE activity_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    activity_id UUID NOT NULL REFERENCES activities(id) ON DELETE CASCADE,
    category_name VARCHAR(100) NOT NULL,
    allocated_amount NUMERIC(12,2) NOT NULL CHECK (allocated_amount >= 0),
    actual_amount NUMERIC(12,2) DEFAULT 0 CHECK (actual_amount >= 0),
    surplus_amount NUMERIC(12,2) DEFAULT 0
);

CREATE TABLE disbursement_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tracking_number VARCHAR(20) UNIQUE DEFAULT ('DR-' || LPAD(nextval('seq_disbursement_request_count')::text, 3, '0')),
    activity_id UUID NOT NULL REFERENCES activities(id) ON DELETE RESTRICT,
    requested_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    status disbursement_request_status DEFAULT 'PENDING',
    approved_by UUID REFERENCES users(id) ON DELETE RESTRICT,
    approved_at TIMESTAMPTZ,
    released_by UUID REFERENCES users(id) ON DELETE RESTRICT,
    released_to UUID REFERENCES users(id) ON DELETE RESTRICT,
    released_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE liquidations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    disbursement_request_id UUID NOT NULL REFERENCES disbursement_requests(id) ON DELETE RESTRICT,
    submitted_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    total_actual NUMERIC(12,2) DEFAULT 0 CHECK (total_actual >= 0),
    total_surplus NUMERIC(12,2) DEFAULT 0,
    status liquidation_status DEFAULT 'DRAFT',
    approved_by UUID REFERENCES users(id) ON DELETE RESTRICT,
    approved_at TIMESTAMPTZ,
    submitted_at TIMESTAMPTZ
);

CREATE TABLE liquidation_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    liquidation_id UUID NOT NULL REFERENCES liquidations(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES activity_categories(id) ON DELETE RESTRICT,
    item_name VARCHAR(150) NOT NULL,
    description TEXT,
    amount NUMERIC(12,2) NOT NULL CHECK (amount >= 0),
    expense_date DATE
);

CREATE TABLE budget_reallocation_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tracking_number VARCHAR(20) UNIQUE DEFAULT ('BRR-' || LPAD(nextval('seq_reallocation_request_count')::text, 3, '0')),
    activity_id UUID NOT NULL REFERENCES activities(id) ON DELETE RESTRICT,
    source_category_id UUID REFERENCES activity_categories(id) ON DELETE RESTRICT,
    destination_category_id UUID NOT NULL REFERENCES activity_categories(id) ON DELETE RESTRICT,
    amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    reason TEXT,
    requested_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    status budget_reallocation_request_status DEFAULT 'PENDING',
    approved_by UUID REFERENCES users(id) ON DELETE RESTRICT,
    approved_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_reallocation_source CHECK (
    (source_category_id IS NOT NULL AND source_category_id <> destination_category_id)
    OR
    (source_category_id IS NULL)
  )
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    action audit_action NOT NULL,
    entity_type audit_entity NOT NULL,
    entity_id UUID NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);