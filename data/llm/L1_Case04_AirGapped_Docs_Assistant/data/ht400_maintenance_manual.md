# Halcyon Turbine Systems - HT-400 Field Maintenance Manual (Synthetic)

> Controlled document. Revision F, issued 12 January 2026. Supersedes Revision E.
> SYNTHETIC document created for training. All part numbers and values are invented.

---

## 1. Scope

This manual covers field maintenance of the HT-400 industrial gas turbine package,
including the compressor section, combustion section, and auxiliary lube system. It
does not cover the generator, the switchgear, or the fuel skid; those are covered by
separate manuals HT-400-GEN and HT-400-FS.

---

## 2. General Safety Requirements

The following apply to **every** procedure in this manual and are not repeated at
each step.

- Isolate and lock off electrical supply at the local isolator before removing any
  guard or cover. Attach a personal lock and retain the key.
- Confirm zero energy state before starting work. A closed valve is not an
  isolation.
- The lube oil system remains pressurised for up to **20 minutes** after shutdown.
  Do not break any lube oil connection until the system pressure gauge reads zero.
- Turbine casing surfaces exceed 300 degrees C during operation and remain above
  60 degrees C for approximately **four hours** after shutdown. Contact with casing
  surfaces inside this period causes burns.
- Two people are required for any lifting operation above 25 kg.

### 2.1 Confined space

The air intake plenum and the exhaust duct are confined spaces. Entry requires a
permit, a gas test, and a standby person outside the space.

---

## 3. Torque Specifications

Values are for clean, dry threads unless a lubricant is specified. Over-torquing
compressor casing bolts distorts the casing and is not recoverable in the field.

| Fastener | Size | Torque | Notes |
|---|---|---|---|
| Compressor casing bolt | M20 | 480 Nm | Tighten in the sequence in section 6.3 |
| Combustion liner bolt | M12 | 95 Nm | Replace on every removal |
| Lube pump flange bolt | M10 | 45 Nm | Anti-seize on threads |
| Bearing cap bolt | M16 | 210 Nm | Do not reuse |
| Exhaust diffuser bolt | M16 | 190 Nm | Nickel anti-seize required |
| Inlet guide vane pivot nut | M8 | 22 Nm | Do not exceed; alloy component |

---

## 4. Fluid Specifications

| Fluid | Specification | Capacity | Change interval |
|---|---|---|---|
| Lube oil | ISO VG 32 turbine grade | 340 litres | 8000 running hours |
| Hydraulic fluid | ISO VG 46 | 45 litres | 12000 running hours |
| Coolant | 40% glycol premix | 120 litres | 24 months |

Lube oil capacity is the **total system** figure including the reservoir and coolers.
The reservoir alone holds 210 litres. Filling to the reservoir figure will leave the
system underfilled and trip the low-level alarm on start.

---

## 5. Scheduled Maintenance Intervals

| Interval | Task group |
|---|---|
| 500 hours | Visual inspection, filter differential check |
| 4000 hours | Borescope inspection of combustion section |
| 8000 hours | Lube oil and filter change, vibration survey |
| 24000 hours | Hot gas path inspection |
| 48000 hours | Major overhaul, compressor rotor removal |

---

## 6. Compressor Section Procedures

### 6.1 Inlet filter element replacement

1. Confirm the unit is shut down and isolated.
2. Open the filter house access door.
3. Remove the retaining clips from each element in turn.
4. Withdraw the element and inspect the sealing face for damage.
5. Fit the replacement element with the airflow arrow pointing downstream.
6. Refit the retaining clips and close the access door.

### 6.2 Inlet guide vane linkage adjustment

1. Confirm the unit is shut down and isolated.
2. Slacken the pivot nut on the linkage arm.
3. Set the vane angle using the alignment gauge, referring to the vane schedule in
   the commissioning record.
4. Retighten the pivot nut to the torque given in section 3.
5. Rotate the linkage by hand through its full travel to confirm free movement.

### 6.3 Compressor casing bolt tightening sequence

Tighten in four passes: 25% of final torque, 50%, 75%, then 100%. Work
diametrically opposite across the flange, not sequentially around it. The final pass
must be completed within 30 minutes of the third to avoid uneven relaxation.

---

## 7. Combustion Section Procedures

### 7.1 Combustion liner removal

1. Confirm the unit is shut down and isolated.
2. Remove the combustion casing cover bolts.
3. Support the liner before removing the final fixings.
4. Withdraw the liner squarely to avoid damaging the transition piece seal.
5. Fit new combustion liner bolts on reassembly; these are not reusable.

### 7.2 Borescope inspection

Insert the borescope through the igniter port. Record any crack indication longer
than 6 mm, any coating loss exceeding 20% of a panel, and any distortion of the
transition piece. Findings above these limits require engineering review before the
unit is returned to service.

---

## 8. Lube System Procedures

### 8.1 Lube oil change

1. Confirm the unit is shut down.
2. Wait for the lube system to depressurise as described in section 2 before
   breaking any connection.
3. Drain the reservoir and the coolers separately; the coolers retain
   approximately 60 litres which will not drain through the reservoir point.
4. Replace both filter elements.
5. Refill to the total system capacity given in section 4, not the reservoir figure.
6. Run the auxiliary lube pump for ten minutes and recheck the level.

### 8.2 Lube pump replacement

1. Confirm the unit is shut down and the lube system has depressurised.
2. Disconnect the suction and discharge flanges.
3. Support the pump and remove the mounting bolts.
4. Fit the replacement pump with new flange gaskets.
5. Torque the flange bolts to the value in section 3, using anti-seize.
6. Prime the pump before starting.

---

## 9. Fault Finding

| Symptom | Probable cause | Action |
|---|---|---|
| Low lube oil pressure at start | Underfilled system, or pump not primed | Check level against total system capacity; prime pump |
| High filter differential | Filter blocked | Replace element at next opportunity; do not exceed 2 bar differential |
| Vibration above alarm | Bearing wear, or rotor imbalance | Carry out vibration survey; do not continue above trip level |
| Exhaust temperature spread high | Fuel nozzle fouling, or liner damage | Borescope combustion section |
| Slow guide vane response | Hydraulic fluid contamination, or linkage binding | Check fluid condition; rotate linkage by hand |

---

## 10. Revision History

| Revision | Date | Change |
|---|---|---|
| D | 2024-03-02 | Initial field issue |
| E | 2025-05-14 | Lube oil change interval revised from 6000 to 8000 hours |
| F | 2026-01-12 | Exhaust diffuser bolt torque revised from 165 Nm to 190 Nm following field reports of joint leakage. Nickel anti-seize now mandatory. |
