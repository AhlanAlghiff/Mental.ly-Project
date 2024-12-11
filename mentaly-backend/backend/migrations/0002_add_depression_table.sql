-- create table if not exists depression (
--     id uuid primary key,
--     user_id varchar(255) not null,
--     age float not null,
--     gender float not null,
--     work_pressure float not null,
--     job_satisfaction float not null,
--     sleep_duration float not null,
--     dietary_habits float not null,
--     have_you_ever_had_suicidal_thoughts boolean not null,
--     work_hours float not null,
--     financial_stress float not null,
--     family_history_of_mental_illness boolean not null,
--     output float not null,
--     created_at timestamp not null,
    
--     foreign key (user_id) references users(id)
-- );

create table if not exists depression (
    id uuid primary key,
    user_id uuid not null, -- Sesuaikan tipe data dengan 'users.id'
    age float not null,
    gender float not null,
    work_pressure float not null,
    job_satisfaction float not null,
    sleep_duration float not null,
    dietary_habits float not null,
    have_you_ever_had_suicidal_thoughts boolean not null,
    work_hours float not null,
    financial_stress float not null,
    family_history_of_mental_illness boolean not null,
    output float not null,
    created_at timestamp not null,
    
    foreign key (user_id) references users(id)
);
