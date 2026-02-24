
export const sortOptionLabels = ["By domain", "By difficulty", "By role"] as const;
export type sortOptionLabelTypes = typeof sortOptionLabels[number];

export const maxQaOptions = ['5', '10', '15', '30', '50'] as const;
export type maxQaOptionsType = typeof maxQaOptions[number];

export const timerOptions = ['15', '20', '30', '45', '60'] as const;
export type timerOptionsType = typeof maxQaOptions[number];

export const sortOptions = ["domainName", "role", "level"] as const;
export type sortOptionType = typeof sortOptions[number];
export const sortOptionsMap: {[key in sortOptionLabelTypes]: sortOptionType} = {
    'By difficulty': 'level',
    'By domain': 'domainName',
    'By role': 'role'
} as const;
